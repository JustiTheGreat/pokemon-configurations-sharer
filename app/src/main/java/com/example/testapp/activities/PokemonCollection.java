package com.example.testapp.activities;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.testapp.R;
import com.example.testapp.Storage;
import com.example.testapp.communication.GetPokemonConfigurations;
import com.example.testapp.data_objects.PokemonConfiguration;
import com.example.testapp.databinding.PokemonCollectionBinding;

import java.util.ArrayList;

public class PokemonCollection extends Fragment {
    private PokemonCollectionBinding binding;
    private GridView gridView;
    private ArrayList<PokemonConfiguration> pokemonConfigurations = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = PokemonCollectionBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        gridView = binding.pokemoncollectionGridviewCollection;
        new GetPokemonConfigurations().execute(this);

        gridView.setOnItemClickListener((adapterView, view1, position, id) -> {
            Storage.setPokemonConfiguration(getPokemonConfiguration(id));
            NavHostFragment
                    .findNavController(PokemonCollection.this)
                    .navigate(R.id.action_pokemonCollection_to_pokemonDetails);
        });

        binding.pokemoncollectionButtonAddbutton.setOnClickListener(v -> NavHostFragment
                .findNavController(PokemonCollection.this)
                .navigate(R.id.action_pokemonCollection_to_addPokemon)
        );
    }

    public PokemonConfiguration getPokemonConfiguration(long id) {
        for (PokemonConfiguration pc : pokemonConfigurations) {
            if (pc.getId() == id)
                return pc;
        }
        return null;
    }

    public void setPokemonConfigurations(ArrayList<PokemonConfiguration> pokemonConfigurations) {
        this.pokemonConfigurations = pokemonConfigurations;
    }

    public void setGridViewAdapter(BaseAdapter gridViewAdapter) {
        gridView.setAdapter(gridViewAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
