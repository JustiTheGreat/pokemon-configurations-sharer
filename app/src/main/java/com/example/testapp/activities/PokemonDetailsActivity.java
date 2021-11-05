package com.example.testapp.activities;

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

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.testapp.Helper;
import com.example.testapp.PokemonConstants;
import com.example.testapp.R;
import com.example.testapp.Storage;
import com.example.testapp.data_objects.Ability;
import com.example.testapp.data_objects.PokemonConfiguration;
import com.example.testapp.databinding.PokemonDetailsBinding;
import com.google.zxing.WriterException;

import java.util.ArrayList;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class PokemonDetailsActivity extends Fragment implements PokemonConstants {
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
        new Helper(this).execute(pokemonConfiguration.getSpecies(), pokemonConfiguration.getAbility());

        //main details
        binding.pokemondetailsTextviewName.setText(pokemonConfiguration.getName());
        String text = pokemonConfiguration.getSpecies()
                + " " + pokemonConfiguration.getGender()
                + " Lv. " + pokemonConfiguration.getLevel();
        binding.pokemondetailsTextviewSpecies.setText(text);
        binding.pokemondetailsTextviewNature.setText(pokemonConfiguration.getNature());

        //click listeners
        int ability_weight = 10, stats_weight = 35, moves_weight = 25;
        binding.ability.setVisibility(View.GONE);
        binding.stats.setVisibility(View.GONE);
        binding.moves.setVisibility(View.GONE);
        if (true) {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) binding.space.getLayoutParams();
            params.weight = 30;
            binding.space.setLayoutParams(params);
        }
        binding.hideAbility.setOnClickListener((v) -> {
            if (binding.ability.getVisibility() == View.GONE) {
                binding.ability.setVisibility(View.VISIBLE);
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) binding.space.getLayoutParams();
                params.weight = params.weight + ability_weight;
                binding.space.setLayoutParams(params);
            } else {
                binding.ability.setVisibility(View.GONE);
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) binding.space.getLayoutParams();
                params.weight = params.weight - ability_weight;
                binding.space.setLayoutParams(params);
            }
        });
        binding.hideStats.setOnClickListener((v) -> {
            if (binding.stats.getVisibility() == View.GONE) {
                binding.stats.setVisibility(View.VISIBLE);
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) binding.space.getLayoutParams();
                params.weight = params.weight + stats_weight;
                binding.space.setLayoutParams(params);
            } else {
                binding.stats.setVisibility(View.GONE);
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) binding.space.getLayoutParams();
                params.weight = params.weight - stats_weight;
                binding.space.setLayoutParams(params);
            }
        });
        binding.hideMoves.setOnClickListener((v) -> {
            if (binding.moves.getVisibility() == View.GONE) {
                binding.moves.setVisibility(View.VISIBLE);
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) binding.space.getLayoutParams();
                params.weight = params.weight + moves_weight;
                binding.space.setLayoutParams(params);
            } else {
                binding.moves.setVisibility(View.GONE);
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) binding.space.getLayoutParams();
                params.weight = params.weight - moves_weight;
                binding.space.setLayoutParams(params);
            }
        });
        binding.pokemondetailsButtonGoback.setOnClickListener(v -> NavHostFragment
                .findNavController(PokemonDetailsActivity.this)
                .navigate(R.id.action_pokemonDetails_to_pokemonCollection));

        binding.pokemondetailsButtonQrcode.setOnClickListener(v-> showQRCode());
    }
    public void showQRCode(){
        Dialog dialog = new Dialog(this.getActivity());
        dialog.setCanceledOnTouchOutside(true);
        dialog.setTitle("QR Code for " + pokemonConfiguration.getName());
        dialog.setContentView(R.layout.qr_code_layout);
        dialog.show();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels/2;
        QRGEncoder qrgEncoder = new QRGEncoder(pokemonConfiguration.toString(), null, QRGContents.Type.TEXT,width);

        try {
            Bitmap bitmap = qrgEncoder.encodeAsBitmap();
            ImageView iv = dialog.findViewById(R.id.qr_image);
            iv.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    public void set(Bitmap bitmap, ArrayList<String> types, Ability ability, ArrayList<Integer> baseStats) {
        //bitmap
        binding.pokemondetailsImageviewPokemon.setImageBitmap(bitmap);
        //types
        binding.pokemondetailsTextviewType1.setText(types.get(0));
        TYPES.forEach(T -> {
            if (T.getName().equalsIgnoreCase(types.get(0))) {
                binding.pokemondetailsTextviewType1.setBackgroundResource(T.getColor());
            }
        });
        if (types.size() == 1) {
            binding.pokemondetailsTextviewType2.setVisibility(View.GONE);
        } else if (types.size() == 2) {
            binding.pokemondetailsTextviewType2.setText(types.get(1));
            TYPES.forEach(T -> {
                if (T.getName().equalsIgnoreCase(types.get(1))) {
                    binding.pokemondetailsTextviewType2.setBackgroundResource(T.getColor());
                }
            });
        }
        //ability
        binding.pokemondetailsTextviewAbilityname.setText(ability.getName());
        binding.pokemondetailsTextviewAbilitydescription.setText(ability.getDescription());
        //stats
        ArrayList<ArrayList<TextView>> statsViews = new ArrayList<ArrayList<TextView>>() {{
            add(
                    new ArrayList<TextView>() {{
                        add(binding.pokemondetailsTextviewTotalhp);
                        add(binding.pokemondetailsTextviewTotalattack);
                        add(binding.pokemondetailsTextviewTotaldefense);
                        add(binding.pokemondetailsTextviewTotalspecialattack);
                        add(binding.pokemondetailsTextviewTotalspecialdefense);
                        add(binding.pokemondetailsTextviewTotalspeed);
                    }}
            );
            add(
                    new ArrayList<TextView>() {{
                        add(binding.pokemondetailsTextviewBasehp);
                        add(binding.pokemondetailsTextviewBaseattack);
                        add(binding.pokemondetailsTextviewBasedefense);
                        add(binding.pokemondetailsTextviewBasespecialattack);
                        add(binding.pokemondetailsTextviewBasespecialdefense);
                        add(binding.pokemondetailsTextviewBasespeed);
                    }}
            );
            add(
                    new ArrayList<TextView>() {{
                        add(binding.pokemondetailsTextviewIvshp);
                        add(binding.pokemondetailsTextviewIvsattack);
                        add(binding.pokemondetailsTextviewIvsdefense);
                        add(binding.pokemondetailsTextviewIvsspecialattack);
                        add(binding.pokemondetailsTextviewIvsspecialdefense);
                        add(binding.pokemondetailsTextviewIvsspeed);
                    }}
            );
            add(
                    new ArrayList<TextView>() {{
                        add(binding.pokemondetailsTextviewEvshp);
                        add(binding.pokemondetailsTextviewEvsattack);
                        add(binding.pokemondetailsTextviewEvsdefense);
                        add(binding.pokemondetailsTextviewEvsspecialattack);
                        add(binding.pokemondetailsTextviewEvsspecialdefense);
                        add(binding.pokemondetailsTextviewEvsspeed);
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
        ArrayList<Double> nature = Helper.getNature(pokemonConfiguration.getNature());
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
