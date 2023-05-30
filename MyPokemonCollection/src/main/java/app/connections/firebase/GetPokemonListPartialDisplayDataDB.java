package app.connections.firebase;

import static android.content.ContentValues.TAG;
import static app.constants.PokemonDatabaseFields.GENDER;
import static app.constants.PokemonDatabaseFields.NAME;
import static app.constants.PokemonDatabaseFields.POKEDEX_NUMBER;
import static app.constants.PokemonDatabaseFields.POKEMON_COLLECTION;
import static app.constants.PokemonDatabaseFields.SHINY;
import static app.constants.PokemonDatabaseFields.USER_ID;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

import app.data_objects.Pokemon;
import app.ui.fragments.ICallbackContext;

public class GetPokemonListPartialDisplayDataDB implements ICallbackContext {

    private final ICallbackContext callbackContext;
    private final String userId;

    public GetPokemonListPartialDisplayDataDB(ICallbackContext callbackContext, String userId) {
        this.callbackContext = callbackContext;
        this.userId = userId;
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    public void execute() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Query query;
        if (userId == null) query = db.collection(POKEMON_COLLECTION);
        else query = db.collection(POKEMON_COLLECTION).whereEqualTo(USER_ID, userId);

        query.get()
                .addOnFailureListener(task -> {
                    Log.e(TAG, task.getMessage());
                    task.printStackTrace();
                    callbackContext.timedOut(this);
                })
                .addOnSuccessListener(task -> {
//                            try {
                                List<Pokemon> pokemonList = new ArrayList<>();
                                List<DocumentSnapshot> documents = task.getDocuments();
                                for (DocumentSnapshot document : documents) {
                                    Pokemon pokemon = Pokemon.newPokemon()
                                            .id(document.getId())
                                            .name(document.getString(NAME))
                                            .pokedexNumber(document.getLong(POKEDEX_NUMBER))
                                            .gender(document.getString(GENDER))
                                            .shiny(Boolean.TRUE.equals(document.getBoolean(SHINY)))
                                            .userId(userId);
                                    pokemonList.add(pokemon);
                                    new GetPokemonHomeArtDB(this, pokemon).execute();
                                }
                                callbackContext.callback(this, pokemonList);
//                            } catch (Exception e) {
//                                Log.e(TAG, e.getMessage());
//                                e.printStackTrace();
//                                callbackContext.timedOut(this);
//                            }
                        }
                );
    }

    @Override
    public void callback(Object caller, Object result) {
    }

    @Override
    public void timedOut(Object caller) {
    }
}
