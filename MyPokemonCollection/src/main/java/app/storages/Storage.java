package app.storages;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.List;

import app.constants.FragmentConstants;
import app.data_objects.Pokemon;
import app.ui.fragments.UtilityFragment;
import lombok.Getter;
import lombok.Setter;

public class Storage implements FragmentConstants {
    private static Pokemon selectedPokemon = null;
    @RequiresApi(api = Build.VERSION_CODES.R)
    public static Pokemon getCopyOfSelectedPokemon(){
        if (selectedPokemon == null) return null;
        else return Pokemon.clone(selectedPokemon);
    }
    @RequiresApi(api = Build.VERSION_CODES.R)
    public static void setCopyOfSelectedPokemon(Pokemon pokemon){
        if (pokemon == null) selectedPokemon = null;
        else selectedPokemon = Pokemon.clone(pokemon);
    }

    @Getter
    @Setter
    private static List<Pokemon> pokemonList = null;
    @Getter
    @Setter
    private static UtilityFragment currentFragment = null;

    private Storage() {
    }
}
