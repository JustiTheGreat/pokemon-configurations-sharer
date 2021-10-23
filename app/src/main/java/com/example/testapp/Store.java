package com.example.testapp;

import com.example.testapp.data_objects.PokemonConfiguration;

public class Store {
    private static PokemonConfiguration pokemonConfiguration;

    public static void setPokemonConfiguration(PokemonConfiguration pc){
        pokemonConfiguration = pc;
    }
    public static PokemonConfiguration getPokemonConfiguration(){
        return pokemonConfiguration;
    }
}
