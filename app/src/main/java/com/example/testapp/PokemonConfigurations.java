package com.example.testapp;

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

import com.example.testapp.databinding.PokemonCollectionBinding;

public class PokemonConfigurations extends Fragment {
    private PokemonCollectionBinding binding;
    private GridView gridView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = PokemonCollectionBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        gridView = binding.pokemonConfigurationsGridViewCollection;
        new GetPokemonConfigurations(this).execute(gridView);
        binding.pokemonCollectionButtonAddButton.setOnClickListener(this::AddNewPokemon);
    }

    public void AddNewPokemon(View view) {

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
