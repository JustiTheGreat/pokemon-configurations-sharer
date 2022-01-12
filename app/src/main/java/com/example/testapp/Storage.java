package com.example.testapp;

import android.annotation.SuppressLint;

import com.example.testapp.constants.FragmentConstants;
import com.example.testapp.data_objects.Pokemon;
import com.example.testapp.fragments.AddPokemon;
import com.example.testapp.fragments.PokemonCollection;
import com.example.testapp.fragments.PokemonDetails;

public class Storage implements FragmentConstants {
    private static String username;
    private static PokemonCollection pokemonCollectionFragment;
    private static PokemonDetails pokemonDetailsFragment;
    @SuppressLint("StaticFieldLeak")
    private static AddPokemon addPokemonFragment;
    private static Pokemon selectedPokemonForDetails;
    private static Pokemon selectedPokemonForAdd;
    private static int currentFragmentType = OTHER;

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        Storage.username = username;
    }

    public static int getCurrentFragmentType() {
        return currentFragmentType;
    }

    public static void setCurrentFragmentType(int currentFragmentType) {
        Storage.currentFragmentType = currentFragmentType;
    }

    public static PokemonCollection getPokemonCollectionFragment() {
        return pokemonCollectionFragment;
    }

    public static void setPokemonCollectionFragment(PokemonCollection pokemonCollectionFragment) {
        currentFragmentType = COLLECTION;
        Storage.pokemonCollectionFragment = pokemonCollectionFragment;
    }

    public static PokemonDetails getPokemonDetailsFragment() {
        return pokemonDetailsFragment;
    }

    public static void setPokemonDetailsFragment(PokemonDetails pokemonDetailsFragment) {
        currentFragmentType = DETAILS;
        Storage.pokemonDetailsFragment = pokemonDetailsFragment;
    }

    public static AddPokemon getAddPokemonFragment() {
        return addPokemonFragment;
    }

    public static void setAddPokemonFragment(AddPokemon fragment) {
        currentFragmentType = ADD;
        addPokemonFragment = fragment;
    }

    public static Pokemon getSelectedPokemonForDetails() {
        return selectedPokemonForDetails;
    }

    public static void setSelectedPokemonForDetails(Pokemon pokemon) {
        selectedPokemonForDetails = pokemon;
    }

    public static Pokemon getSelectedPokemonForAdd() {
        Pokemon pokemon = selectedPokemonForAdd;
        selectedPokemonForAdd = null;
        return pokemon;
    }

    public static void setSelectedPokemonForAdd(Pokemon pokemon) {
        selectedPokemonForAdd = pokemon;
    }

    public static boolean pokemonIsSelectedForDetails() {
        return selectedPokemonForDetails != null;
    }

    public static boolean pokemonIsSelectedForAdd() {
        return selectedPokemonForAdd != null;
    }
}
