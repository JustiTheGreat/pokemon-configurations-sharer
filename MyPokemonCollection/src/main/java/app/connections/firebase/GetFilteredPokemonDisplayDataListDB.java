package app.connections.firebase;

import static android.content.ContentValues.TAG;
import static app.constants.PokemonConstants.DEFAULT_POKEDEX_NUMBER;
import static app.constants.PokemonDatabaseFields.GENDER;
import static app.constants.PokemonDatabaseFields.NAME;
import static app.constants.PokemonDatabaseFields.POKEDEX_NUMBER;
import static app.constants.PokemonDatabaseFields.POKEMON_COLLECTION;
import static app.constants.PokemonDatabaseFields.PUBLIC_POKEMON_COLLECTION;
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

public class GetFilteredPokemonDisplayDataListDB {

    private final ICallbackContext callbackContext;
    private final long pokedexNumber;

    public GetFilteredPokemonDisplayDataListDB(ICallbackContext callbackContext, long pokedexNumber) {
        this.callbackContext = callbackContext;
        this.pokedexNumber = pokedexNumber;
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    public void execute() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Query query;
        if (pokedexNumber == DEFAULT_POKEDEX_NUMBER) {
            query = db.collection(PUBLIC_POKEMON_COLLECTION);
        } else {
            query = db.collection(PUBLIC_POKEMON_COLLECTION).whereEqualTo(POKEDEX_NUMBER, pokedexNumber);
        }

        query.get()
                .addOnFailureListener(task -> {
                    Log.e(TAG, task.getMessage());
                    task.printStackTrace();
                    callbackContext.timedOut(this);
                })
                .addOnSuccessListener(task -> {
                            try {
                                List<Pokemon> pokemonList = new ArrayList<>();
                                List<DocumentSnapshot> documents = task.getDocuments();
                                for (DocumentSnapshot document : documents) {
                                    Pokemon pokemon = Pokemon.newPokemon()
                                            .id(document.getId())
                                            .name(document.getString(NAME))
                                            .pokedexNumber(document.getLong(POKEDEX_NUMBER))
                                            .gender(document.getString(GENDER))
                                            .shiny(document.getBoolean(SHINY))
                                            .userId(document.getString(USER_ID));
                                    pokemonList.add(pokemon);
                                    new GetPokemonHomeArtDB(callbackContext, pokemon).execute();
                                }
                                callbackContext.callback(this, pokemonList);
                            } catch (RuntimeException e) {
                                Log.e(TAG, e.getMessage());
                                e.printStackTrace();
                                callbackContext.timedOut(this);
                            }
                        }
                );
    }
}
