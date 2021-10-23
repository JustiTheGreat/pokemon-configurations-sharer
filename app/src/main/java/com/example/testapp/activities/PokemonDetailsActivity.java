package com.example.testapp.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.testapp.R;
import com.example.testapp.Store;
import com.example.testapp.data_objects.PokemonConfiguration;
import com.example.testapp.databinding.PokemonDetailsBinding;

public class PokemonDetailsActivity extends Fragment {
    private PokemonConfiguration pokemonConfiguration;
    private PokemonDetailsBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = PokemonDetailsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pokemonConfiguration=Store.getPokemonConfiguration();
        binding.pokemondetailsTextviewName.setText(pokemonConfiguration.getName());
        //binding.pokemondetailsImageviewPokemon.setImageBitmap(null);
        binding.pokemondetailsTextviewSpecies.setText(pokemonConfiguration.getSpecies());
        binding.pokemondetailsTextviewGender.setText(pokemonConfiguration.getGender());
        binding.pokemondetailsTextviewLevel.setText(String.valueOf(pokemonConfiguration.getLevel()));
        binding.pokemondetailsTextviewNature.setText(pokemonConfiguration.getNature().getName());
        binding.pokemondetailsTextviewAbilityname.setText(pokemonConfiguration.getAbility().getName());
        //binding.pokemondetailsTextviewAbilitydescription.setText(null);
        //stats
        //basestats
        //ivs
        //evs
        int ability_weight=20,stats_weight=25,moves_weight=25;
        int space_weight=100;
        binding.ability.setVisibility(View.GONE);
        binding.stats.setVisibility(View.GONE);
        binding.moves.setVisibility(View.GONE);

        if(true) {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) binding.space.getLayoutParams();
            params.weight = 30;
            binding.space.setLayoutParams(params);
        }

        binding.hideAbility.setOnClickListener((v)->{

            if(binding.ability.getVisibility()==View.GONE){
                binding.ability.setVisibility(View.VISIBLE);
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) binding.space.getLayoutParams();
                params.weight = params.weight+ability_weight;
                binding.space.setLayoutParams(params);
            }
            else {
                binding.ability.setVisibility(View.GONE);
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) binding.space.getLayoutParams();
                params.weight = params.weight-ability_weight;
                binding.space.setLayoutParams(params);
            }
        });
        binding.hideStats.setOnClickListener((v)->{
            if(binding.stats.getVisibility()==View.GONE){
                binding.stats.setVisibility(View.VISIBLE);
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) binding.space.getLayoutParams();
                params.weight = params.weight+stats_weight;
                binding.space.setLayoutParams(params);
            }
            else {
                binding.stats.setVisibility(View.GONE);
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) binding.space.getLayoutParams();
                params.weight = params.weight-stats_weight;
                binding.space.setLayoutParams(params);
            }
        });
        binding.hideMoves.setOnClickListener((v)->{
            if(binding.moves.getVisibility()==View.GONE){
                binding.moves.setVisibility(View.VISIBLE);
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) binding.space.getLayoutParams();
                params.weight = params.weight+moves_weight;
                binding.space.setLayoutParams(params);
            }
            else {
                binding.moves.setVisibility(View.GONE);
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) binding.space.getLayoutParams();
                params.weight = params.weight-moves_weight;
                binding.space.setLayoutParams(params);
            }
        });

        binding.pokemondetailsButtonGoback.setOnClickListener(v -> NavHostFragment
                .findNavController(PokemonDetailsActivity.this)
                .navigate(R.id.action_pokemonDetails_to_pokemonCollection));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
