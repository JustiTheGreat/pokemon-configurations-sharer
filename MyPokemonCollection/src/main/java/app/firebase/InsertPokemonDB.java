package app.firebase;

import static app.constants.PokemonDatabaseFields.POKEMON_COLLECTION;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.firebase.firestore.FirebaseFirestore;

import app.data_objects.Pokemon;
import app.ui.fragments.ICallbackContext;

public class InsertPokemonDB {

    private final ICallbackContext callbackContext;
    private final Pokemon pokemon;

    public InsertPokemonDB(ICallbackContext callbackContext, Pokemon pokemon){
        this.callbackContext = callbackContext;
        this.pokemon = pokemon;
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    public void execute() {
        FirebaseFirestore.getInstance().collection(POKEMON_COLLECTION)
                .add(pokemon.getDatabaseMapObject())
                .addOnSuccessListener(task -> {
                    pokemon.setID(task.getId());
                    //todo get image
                    callbackContext.callback(this, pokemon);})
                .addOnFailureListener(task -> callbackContext.timedOut(this));
    }
}
