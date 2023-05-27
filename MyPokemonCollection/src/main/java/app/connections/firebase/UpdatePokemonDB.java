package app.connections.firebase;

import static app.constants.PokemonDatabaseFields.POKEMON_COLLECTION;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.firebase.firestore.FirebaseFirestore;

import app.data_objects.Pokemon;
import app.ui.fragments.ICallbackContext;

public class UpdatePokemonDB {

    private final ICallbackContext callbackContext;
    private final Pokemon pokemon;

    public UpdatePokemonDB(ICallbackContext callbackContext, Pokemon pokemon) {
        this.callbackContext = callbackContext;
        this.pokemon = pokemon;
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    public void execute() {
        FirebaseFirestore.getInstance().collection(POKEMON_COLLECTION)
                .document(pokemon.getID())
                .set(pokemon.getDatabaseMapObject())
                .addOnSuccessListener(task -> callbackContext.callback(this, pokemon))
                .addOnFailureListener(task -> callbackContext.timedOut(this));
    }
}
