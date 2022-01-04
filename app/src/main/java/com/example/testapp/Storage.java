package com.example.testapp;

import androidx.appcompat.app.AppCompatActivity;

import com.example.testapp.data_objects.Pokemon;
import com.example.testapp.fragments.AddPokemon;

public class Storage {
    private static AppCompatActivity activity;
    private static AddPokemon addPokemonFragment;
    private static Pokemon selectedPokemonForDetails;
    private static Pokemon selectedPokemonForAdd;

    public static void setActivity(AppCompatActivity appCompatActivity) {
        activity = appCompatActivity;
    }

    public static AppCompatActivity getActivity() {
        return activity;
    }

    public static void setAddPokemonFragment(AddPokemon fragment) {
        addPokemonFragment = fragment;
    }

    public static AddPokemon getAddPokemonFragment() {
        return addPokemonFragment;
    }

    public static void setSelectedPokemonForDetails(Pokemon pokemon) {
        selectedPokemonForDetails = pokemon;
    }

    public static void setSelectedPokemonForAdd(Pokemon pokemon) {
        selectedPokemonForAdd = pokemon;
    }

    public static Pokemon getSelectedPokemonForDetails() {
        return selectedPokemonForDetails;
    }

    public static Pokemon getSelectedPokemonForAdd() {
        Pokemon pokemon = selectedPokemonForAdd;
        selectedPokemonForAdd = null;
        return pokemon;
    }

    public static boolean pokemonIsSelectedForDetails() {
        return selectedPokemonForDetails != null;
    }

    public static boolean pokemonIsSelectedForAdd() {
        return selectedPokemonForAdd != null;
    }
}
