package app.firebase;

import static app.constants.PokemonDatabaseFields.POKEMON_COLLECTION;

import com.google.firebase.firestore.FirebaseFirestore;

import app.ui.fragments.ICallbackContext;

public class DeletePokemonDB {

    private final ICallbackContext callbackContext;
    private final String pokemonId;

    public DeletePokemonDB(ICallbackContext callbackContext, String pokemonId) {
        this.callbackContext = callbackContext;
        this.pokemonId = pokemonId;
    }

    public void execute() {
        FirebaseFirestore.getInstance().collection(POKEMON_COLLECTION)
                .document(pokemonId)
                .delete()
                .addOnSuccessListener(task -> callbackContext.callback(this, pokemonId))
                .addOnFailureListener(task -> callbackContext.timedOut(this));
    }
}