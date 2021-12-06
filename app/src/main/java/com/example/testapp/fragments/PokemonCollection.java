package com.example.testapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.testapp.R;
import com.example.testapp.SelectedPokemon;
import com.example.testapp.async_tasks.GetPokemonList;
import com.example.testapp.data_objects.Pokemon;
import com.example.testapp.databinding.CollectionBinding;

import java.util.ArrayList;

public class PokemonCollection extends Fragment {
    private CollectionBinding binding;
    private GridView collectionGridView;
    private ArrayList<Pokemon> pokemonList = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = CollectionBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        collectionGridView = binding.collectionGridview;
        new GetPokemonList().execute(this);

        collectionGridView.setOnItemClickListener(this::seePokemonDetails);
        binding.collectionButton.setOnClickListener(this::addNewPokemon);
    }

    private void seePokemonDetails(AdapterView<?> adapterView, View view, int position, long id) {
        SelectedPokemon.setPokemon(getPokemon(id));
        NavHostFragment
                .findNavController(PokemonCollection.this)
                .navigate(R.id.action_pokemonCollection_to_pokemonDetails);
    }

    private void addNewPokemon(View view) {
        NavHostFragment
                .findNavController(PokemonCollection.this)
                .navigate(R.id.action_pokemonCollection_to_addPokemon);
    }

    private Pokemon getPokemon(long id) {
        for (Pokemon pokemon : pokemonList) {
            if (pokemon.getID() == id)
                return pokemon;
        }
        return null;
    }

    public void setPokemonList(ArrayList<Pokemon> pokemonList) {
        this.pokemonList = pokemonList;
    }

    public void setCollectionGridViewAdapter(BaseAdapter gridViewAdapter) {
        collectionGridView.setAdapter(gridViewAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
