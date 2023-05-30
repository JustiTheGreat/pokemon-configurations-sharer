package app.storages;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;

import app.data_objects.Pokemon;

public class Storage {

    private static List<Pokemon> pokemonList = null;
    private static Pokemon selectedPokemon = null;
    private static boolean publicPokemon = false;
    private static List<Pokemon> pokemonSpeciesList = null;

    private Storage() {
    }

    public static List<Pokemon> getPokemonList() {
        return pokemonList;
    }

    public static void setPokemonList(List<Pokemon> pokemonList) {
        Storage.pokemonList = pokemonList;
    }

    public static void addToPokemonList(Pokemon pokemon) {
        Storage.pokemonList.add(pokemon);
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    public static void setInPokemonList(Pokemon pokemon) {
        for (int i = 0; i < pokemonList.size(); i++) {
            if (pokemonList.get(i).getID().equals(pokemon.getID())) {
                pokemonList.set(i, pokemon);
                break;
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    public static void removeByIdFromPokemonList(String pokemonId) {
        for (int i = 0; i < pokemonList.size(); i++) {
            if (pokemonList.get(i).getID().equals(pokemonId)) {
                pokemonList.remove(i);
                break;
            }
        }
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

    public static boolean isPublicPokemon() {
        return publicPokemon;
    }

    public static void setPublicPokemon(boolean publicPokemon) {
        Storage.publicPokemon = publicPokemon;
    }

    public static List<Pokemon> getPokemonSpeciesList() {
        return pokemonSpeciesList == null ? null : new ArrayList<>(pokemonSpeciesList);
    }

    public static void setPokemonSpeciesList(List<Pokemon> pokemonSpeciesList) {
        Storage.pokemonSpeciesList = pokemonSpeciesList;
    }
}
