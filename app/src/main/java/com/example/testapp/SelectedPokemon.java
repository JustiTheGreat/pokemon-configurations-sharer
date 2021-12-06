package com.example.testapp;

import com.example.testapp.data_objects.Pokemon;

public class SelectedPokemon {
    private static Pokemon pokemonConfiguration;

    public static void setPokemon(Pokemon pc) {
        pokemonConfiguration = pc;
    }

    public static Pokemon getPokemon() {
        return pokemonConfiguration;
    }
}
