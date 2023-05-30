package app.connections.firebase;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.firebase.firestore.FirebaseFirestore;

import app.data_objects.Pokemon;
import app.ui.fragments.ICallbackContext;

public class InsertPokemonDB {

    private final ICallbackContext callbackContext;
    private final Pokemon pokemon;
    private final String databaseCollection;

    public InsertPokemonDB(ICallbackContext callbackContext, Pokemon pokemon, String databaseCollection) {
        this.callbackContext = callbackContext;
        this.pokemon = pokemon;
        this.databaseCollection = databaseCollection;
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    public void execute() {
        FirebaseFirestore.getInstance().collection(databaseCollection)
                .add(pokemon.getDatabaseMapObject())
                .addOnSuccessListener(task -> callbackContext.callback(this, pokemon))
                .addOnFailureListener(task -> callbackContext.timedOut(this));
    }
}
