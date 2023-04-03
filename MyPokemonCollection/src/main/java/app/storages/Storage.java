package app.storages;

import java.util.List;

import app.constants.FragmentConstants;
import app.data_objects.Pokemon;
import lombok.Getter;
import lombok.Setter;

public class Storage implements FragmentConstants {
//    private static PokemonCollection pokemonCollectionFragment;
//    private static PokemonDetails pokemonDetailsFragment;
    //private static AddPokemon addPokemonFragment;
//    @Getter @Setter private static Pokemon selectedPokemonForDetails;

    @Getter @Setter private static Pokemon selectedPokemon = null;
    @Getter @Setter private static boolean changesMade = false;
    @Getter @Setter private static List<Pokemon> pokemonList = null;
//    private static Pokemon selectedPokemonForAdd;
//    private static int currentFragmentType = OTHER;


    private Storage(){}

//    public static int getCurrentFragmentType() {
//        return currentFragmentType;
//    }
//
//    public static void setCurrentFragmentType(int currentFragmentType) {
//        Storage.currentFragmentType = currentFragmentType;
//    }

//    public static PokemonCollection getPokemonCollectionFragment() {
//        return pokemonCollectionFragment;
//    }
//
//    public static void setPokemonCollectionFragment(PokemonCollection pokemonCollectionFragment) {
//        currentFragmentType = COLLECTION;
//        Storage.pokemonCollectionFragment = pokemonCollectionFragment;
//    }
//
//    public static PokemonDetails getPokemonDetailsFragment() {
//        return pokemonDetailsFragment;
//    }
//
//    public static void setPokemonDetailsFragment(PokemonDetails pokemonDetailsFragment) {
//        currentFragmentType = DETAILS;
//        Storage.pokemonDetailsFragment = pokemonDetailsFragment;
//    }

//    public static AddPokemon getAddPokemonFragment() {
//        return addPokemonFragment;
//    }
//
//    public static void setAddPokemonFragment(AddPokemon fragment) {
//        currentFragmentType = ADD;
//        addPokemonFragment = fragment;
//    }

//    public static Pokemon getSelectedPokemonForAdd() {
//        Pokemon pokemon = selectedPokemonForAdd;
//        selectedPokemonForAdd = null;
//        return pokemon;
//    }
//
//    public static void setSelectedPokemonForAdd(Pokemon pokemon) {
//        selectedPokemonForAdd = pokemon;
//    }
//
//    public static boolean pokemonIsSelectedForAdd() {
//        return selectedPokemonForAdd != null;
//    }
}
