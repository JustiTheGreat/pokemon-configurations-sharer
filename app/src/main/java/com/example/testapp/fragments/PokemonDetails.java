package com.example.testapp.fragments;

import static com.example.testapp.PokemonConstants.NUMBER_OF_STATS;

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

import com.example.testapp.R;
import com.example.testapp.SelectedPokemon;
import com.example.testapp.async_tasks.Helper;
import com.example.testapp.data_objects.Pokemon;
import com.example.testapp.databinding.PokemonDetailsBinding;
import com.google.zxing.WriterException;

import java.util.ArrayList;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class PokemonDetails extends Fragment {
    private Pokemon pokemon;
    private PokemonDetailsBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = PokemonDetailsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }


    @RequiresApi(api = Build.VERSION_CODES.R)
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pokemon = SelectedPokemon.getPokemon();
        setPageInfo();
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
        binding.pokemondetailsDelete.setOnClickListener(v -> deletePokemon());
        binding.pokemondetailsQrcode.setOnClickListener(v -> showQRCode());
        binding.pokemondetailsEdit.setOnClickListener(v -> editPokemon());
    }

    private void deletePokemon() {
        Dialog dialog = new Dialog(this.getActivity());
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.dialog_delete);
        dialog.show();

        dialog.findViewById(R.id.d_delete_yes).setOnClickListener(v -> {
            //delete from database
            Toast.makeText(this.getActivity(), pokemon.getName() + "was deleted!", Toast.LENGTH_SHORT).show();
        });
        dialog.findViewById(R.id.d_delete_no).setOnClickListener(v -> dialog.dismiss());
    }

    private void showQRCode() {
        Dialog dialog = new Dialog(this.getActivity());
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.dialog_qr);
        dialog.show();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        requireActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels / 2;
        QRGEncoder qrgEncoder = new QRGEncoder(pokemon.toString(), null, QRGContents.Type.TEXT, width);

        try {
            Bitmap bitmap = qrgEncoder.encodeAsBitmap();
            ImageView iv = dialog.findViewById(R.id.d_qr_image);
            iv.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    private void editPokemon() {
        Toast.makeText(this.getActivity(), "Edit not yet implemented!", Toast.LENGTH_SHORT).show();
    }


    @RequiresApi(api = Build.VERSION_CODES.R)
    private void setPageInfo() {
        binding.pokemondetailsName.setText(pokemon.getName());
        binding.pokemondetailsImage.setImageBitmap(pokemon.getOfficialArt());

        String text = pokemon.getSpecies()
                + " " + pokemon.getGender()
                + " Lv. " + pokemon.getLevel();
        binding.pokemondetailsSpecies.setText(text);
        binding.pokemondetailsNature.setText(pokemon.getNature().getName());

        binding.pokemondetailsType1.setText(pokemon.getTypes().get(0).getName().toUpperCase());
        binding.pokemondetailsType1.setBackgroundResource(pokemon.getTypes().get(0).getColor());
        if (pokemon.getTypes().size() == 1) {
            binding.pokemondetailsType2.setVisibility(View.GONE);
        } else {
            binding.pokemondetailsType2.setText(pokemon.getTypes().get(1).getName().toUpperCase());
            binding.pokemondetailsType2.setBackgroundResource(pokemon.getTypes().get(1).getColor());
        }

        binding.pokemondetailsAbilityname.setText(pokemon.getAbility().getName());
        binding.pokemondetailsAbilitydescription.setText(pokemon.getAbility().getDescription());

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

        for (int i = 0; i < NUMBER_OF_STATS; i++) {
            statsViews.get(1).get(i).setText(String.valueOf(pokemon.getBaseStats().get(i)));
            statsViews.get(2).get(i).setText(String.valueOf(pokemon.getIVs().get(i)));
            statsViews.get(3).get(i).setText(String.valueOf(pokemon.getEVs().get(i)));
        }
        ArrayList<Integer> totalStats = Helper.calculateStats(
                pokemon.getBaseStats(),
                pokemon.getIVs(),
                pokemon.getEVs(),
                pokemon.getLevel(),
                pokemon.getNature());
        for (int i = 0; i < NUMBER_OF_STATS; i++) {
            statsViews.get(0).get(i).setText(String.valueOf(totalStats.get(i)));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
