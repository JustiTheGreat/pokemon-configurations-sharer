package app;

import app.constants.FragmentConstants;
import app.data_objects.Pokemon;
import app.ui.fragments.PokemonCollection;
import app.ui.fragments.PokemonDetails;
import lombok.Getter;
import lombok.Setter;

public class Storage implements FragmentConstants {
    private static String username;
    private static PokemonCollection pokemonCollectionFragment;
    private static PokemonDetails pokemonDetailsFragment;
    //private static AddPokemon addPokemonFragment;
    @Getter @Setter private static Pokemon selectedPokemonForDetails;
    private static Pokemon selectedPokemonForAdd;
    private static int currentFragmentType = OTHER;

    private Storage(){}

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

//    public static AddPokemon getAddPokemonFragment() {
//        return addPokemonFragment;
//    }
//
//    public static void setAddPokemonFragment(AddPokemon fragment) {
//        currentFragmentType = ADD;
//        addPokemonFragment = fragment;
//    }

    public static Pokemon getSelectedPokemonForAdd() {
        Pokemon pokemon = selectedPokemonForAdd;
        selectedPokemonForAdd = null;
        return pokemon;
    }

    public static void setSelectedPokemonForAdd(Pokemon pokemon) {
        selectedPokemonForAdd = pokemon;
    }

    public static boolean pokemonIsSelectedForAdd() {
        return selectedPokemonForAdd != null;
    }
}
