package com.example.testapp.fragments;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.testapp.data_objects.NATURE;
import com.example.testapp.async_tasks.Helper;
import com.example.testapp.PokemonConstants;
import com.example.testapp.R;
import com.example.testapp.Storage;
import com.example.testapp.async_tasks.GetPokemonDetails;
import com.example.testapp.data_objects.Ability;
import com.example.testapp.data_objects.PokemonConfiguration;
import com.example.testapp.databinding.PokemonDetailsBinding;
import com.google.zxing.WriterException;

import java.util.ArrayList;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class PokemonDetails extends Fragment implements PokemonConstants {
    private PokemonConfiguration pokemonConfiguration;
    private PokemonDetailsBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = PokemonDetailsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pokemonConfiguration = Storage.getPokemonConfiguration();
        new GetPokemonDetails().execute(this, pokemonConfiguration.getSpecies(), pokemonConfiguration.getAbility());

        binding.pokemondetailsName.setText(pokemonConfiguration.getName());
        String text = pokemonConfiguration.getSpecies()
                + " " + pokemonConfiguration.getGender()
                + " Lv. " + pokemonConfiguration.getLevel();
        binding.pokemondetailsSpecies.setText(text);
        binding.pokemondetailsNature.setText(pokemonConfiguration.getNatureName());
        //adapting layouts when open clicking one
        {
            LinearLayout ability = binding.pokemondetailsAbility;
            LinearLayout hideAbility = binding.pokemondetailsHideability;
            LinearLayout stats = binding.pokemondetailsStats;
            LinearLayout hideStats = binding.pokemondetailsHidestats;
            LinearLayout moves = binding.pokemondetailsMoves;
            LinearLayout hideMoves = binding.pokemondetailsHidemoves;
            LinearLayout space = binding.pokemondetailsSpace;

            int ability_weight = 7, stats_weight = 27, moves_weight = 16;
            int space_weight = 100 - ability_weight - stats_weight - moves_weight;
            ability.setVisibility(View.GONE);
            stats.setVisibility(View.GONE);
            moves.setVisibility(View.GONE);
            {
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) space.getLayoutParams();
                params.weight = space_weight;
                space.setLayoutParams(params);
            }
            hideAbility.setOnClickListener(v -> {
                if (ability.getVisibility() == View.GONE) {
                    ability.setVisibility(View.VISIBLE);
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) space.getLayoutParams();
                    params.weight = params.weight + ability_weight;
                    space.setLayoutParams(params);
                } else {
                    ability.setVisibility(View.GONE);
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) space.getLayoutParams();
                    params.weight = params.weight - ability_weight;
                    space.setLayoutParams(params);
                }
            });
            hideStats.setOnClickListener(v -> {
                if (stats.getVisibility() == View.GONE) {
                    stats.setVisibility(View.VISIBLE);
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) space.getLayoutParams();
                    params.weight = params.weight + stats_weight;
                    space.setLayoutParams(params);
                } else {
                    stats.setVisibility(View.GONE);
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) space.getLayoutParams();
                    params.weight = params.weight - stats_weight;
                    space.setLayoutParams(params);
                }
            });
            hideMoves.setOnClickListener(v -> {
                if (moves.getVisibility() == View.GONE) {
                    moves.setVisibility(View.VISIBLE);
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) space.getLayoutParams();
                    params.weight = params.weight + moves_weight;
                    space.setLayoutParams(params);
                } else {
                    moves.setVisibility(View.GONE);
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) space.getLayoutParams();
                    params.weight = params.weight - moves_weight;
                    space.setLayoutParams(params);
                }
            });
        }
        //end of adapting layouts
        binding.pokemondetailsQrcode.setOnClickListener(v -> showQRCode());
        binding.pokemondetailsDelete.setOnClickListener(v -> deletePokemon());
    }

    public void deletePokemon() {
        Dialog dialog = new Dialog(this.getActivity());
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.dialog_delete);
        dialog.show();

        dialog.findViewById(R.id.d_delete_yes).setOnClickListener(v -> {
            //delete from database
            Toast toast = Toast.makeText(this.getActivity(), pokemonConfiguration.getName() + "was deleted!", Toast.LENGTH_LONG);
        });
        dialog.findViewById(R.id.d_delete_no).setOnClickListener(v -> dialog.dismiss());
    }

    public void showQRCode() {
        Dialog dialog = new Dialog(this.getActivity());
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.dialog_qr);
        dialog.show();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels / 2;
        QRGEncoder qrgEncoder = new QRGEncoder(pokemonConfiguration.toString(), null, QRGContents.Type.TEXT, width);

        try {
            Bitmap bitmap = qrgEncoder.encodeAsBitmap();
            ImageView iv = dialog.findViewById(R.id.d_qr_image);
            iv.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    public void set(Bitmap bitmap, ArrayList<String> types, Ability ability, ArrayList<Integer> baseStats) {
        //bitmap
        binding.pokemondetailsImage.setImageBitmap(bitmap);
        //types
        binding.pokemondetailsType1.setText(types.get(0));
        TYPES.forEach(T -> {
            if (T.getName().equalsIgnoreCase(types.get(0))) {
                binding.pokemondetailsType1.setBackgroundResource(T.getColor());
            }
        });
        if (types.size() == 1) {
            binding.pokemondetailsType2.setVisibility(View.GONE);
        } else if (types.size() == 2) {
            binding.pokemondetailsType2.setText(types.get(1));
            TYPES.forEach(T -> {
                if (T.getName().equalsIgnoreCase(types.get(1))) {
                    binding.pokemondetailsType2.setBackgroundResource(T.getColor());
                }
            });
        }
        //ability
        binding.pokemondetailsAbilityname.setText(ability.getName());
        binding.pokemondetailsAbilitydescription.setText(ability.getDescription());
        //stats
        ArrayList<ArrayList<TextView>> statsViews = new ArrayList<ArrayList<TextView>>() {{
            add(
                    new ArrayList<TextView>() {{
                        add(binding.pokemondetailsTotalhp);
                        add(binding.pokemondetailsTotalattack);
                        add(binding.pokemondetailsTotaldefense);
                        add(binding.pokemondetailsTotalspecialattack);
                        add(binding.pokemondetailsTotalspecialdefense);
                        add(binding.pokemondetailsTotalspeed);
                    }}
            );
            add(
                    new ArrayList<TextView>() {{
                        add(binding.pokemondetailsBasehp);
                        add(binding.pokemondetailsBaseattack);
                        add(binding.pokemondetailsBasedefense);
                        add(binding.pokemondetailsBasespecialattack);
                        add(binding.pokemondetailsBasespecialdefense);
                        add(binding.pokemondetailsBasespeed);
                    }}
            );
            add(
                    new ArrayList<TextView>() {{
                        add(binding.pokemondetailsIvshp);
                        add(binding.pokemondetailsIvsattack);
                        add(binding.pokemondetailsIvsdefense);
                        add(binding.pokemondetailsIvsspecialattack);
                        add(binding.pokemondetailsIvsspecialdefense);
                        add(binding.pokemondetailsIvsspeed);
                    }}
            );
            add(
                    new ArrayList<TextView>() {{
                        add(binding.pokemondetailsEvshp);
                        add(binding.pokemondetailsEvsattack);
                        add(binding.pokemondetailsEvsdefense);
                        add(binding.pokemondetailsEvsspecialattack);
                        add(binding.pokemondetailsEvsspecialdefense);
                        add(binding.pokemondetailsEvsspeed);
                    }}
            );
        }};

        for (int i = 0; i < baseStats.size(); i++) {
            statsViews.get(1).get(i).setText(String.valueOf(baseStats.get(i)));
        }
        for (int i = 0; i < pokemonConfiguration.getIVs().size(); i++) {
            statsViews.get(2).get(i).setText(String.valueOf(pokemonConfiguration.getIVs().get(i)));
        }
        for (int i = 0; i < pokemonConfiguration.getEVs().size(); i++) {
            statsViews.get(3).get(i).setText(String.valueOf(pokemonConfiguration.getEVs().get(i)));
        }
        ArrayList<Double> nature = NATURE.getNature(pokemonConfiguration.getNatureName()).getEffects();
        ArrayList<Integer> totalStats = Helper.calculateStats(
                baseStats,
                pokemonConfiguration.getIVs(),
                pokemonConfiguration.getEVs(),
                pokemonConfiguration.getLevel(),
                nature);
        for (int i = 0; i < totalStats.size(); i++) {
            statsViews.get(0).get(i).setText(String.valueOf(totalStats.get(i)));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
