package com.example.testapp;

import com.example.testapp.data_objects.Pokemon;

public class Storage {
    private static Pokemon selectedPokemon;
    private static boolean wasScanned = false;

    public static void setSelectedPokemon(Pokemon pc) {
        selectedPokemon = pc;
    }

    public static Pokemon getSelectedPokemon() {
        Pokemon pokemon = selectedPokemon;
        selectedPokemon = null;
        return pokemon;
    }

    public static boolean pokemonIsSelected() {
        return selectedPokemon != null;
    }

    public static boolean wasScanned() {
        return wasScanned;
    }

    public static void setScanned(boolean scanned) {
        wasScanned = scanned;
    }
}
