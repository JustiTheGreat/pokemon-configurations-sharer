package app.storages;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.List;

import app.data_objects.Pokemon;

public class Storage {

    private static List<Pokemon> pokemonList = null;
    private static Pokemon selectedPokemon = null;

    private Storage() {
    }

    public static List<Pokemon> getPokemonList() {
        return pokemonList;
    }

    public static void setPokemonList(List<Pokemon> pokemonList) {
        Storage.pokemonList = pokemonList;
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    public static Pokemon getCopyOfSelectedPokemon() {
        if (selectedPokemon == null) return null;
        else return Pokemon.clone(selectedPokemon);
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    public static void setCopyOfSelectedPokemon(Pokemon pokemon) {
        if (pokemon == null) selectedPokemon = null;
        else selectedPokemon = Pokemon.clone(pokemon);
    }
}
