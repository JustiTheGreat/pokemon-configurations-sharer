package com.example.testapp;

import com.example.testapp.data_objects.Pokemon;

public class Storage {
    private static Pokemon selectedPokemonForDetails;
    private static Pokemon selectedPokemonForAdd;

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
