package app.connections.firebase;

import static android.content.ContentValues.TAG;
import static app.constants.PokemonDatabaseFields.ABILITY;
import static app.constants.PokemonDatabaseFields.EVS;
import static app.constants.PokemonDatabaseFields.IVS;
import static app.constants.PokemonDatabaseFields.LEVEL;
import static app.constants.PokemonDatabaseFields.MOVES;
import static app.constants.PokemonDatabaseFields.NATURE;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.stream.Collectors;

import app.data_objects.Ability;
import app.data_objects.Move;
import app.data_objects.Nature;
import app.data_objects.Pokemon;
import app.ui.fragments.ICallbackContext;

public class GetOtherPokemonDataDB  implements ICallbackContext {

    private final ICallbackContext callbackContext;
    private final String pokemonId;
    private final String collection;

    public GetOtherPokemonDataDB(ICallbackContext callbackContext, String pokemonId, String collection) {
        this.callbackContext = callbackContext;
        this.pokemonId = pokemonId;
        this.collection = collection;
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    public void execute() {
        FirebaseFirestore.getInstance().collection(collection).document(pokemonId).get()
                .addOnFailureListener(task -> {
                    Log.e(TAG, task.getMessage());
                    task.printStackTrace();
                    callbackContext.timedOut(this);
                })
                .addOnSuccessListener(task -> {
                    try {
                        Pokemon pokemon = Pokemon.newPokemon()
                                .level(task.getLong(LEVEL))
                                .ability(new Ability(task.getString(ABILITY)))
                                .nature(Nature.getNature(task.getString(NATURE)))
                                .ivs((List<Long>) task.get(IVS))
                                .evs((List<Long>) task.get(EVS))
                                .moves(((List<String>) task.get(MOVES)).stream()
                                        .map(name -> new Move(name))
                                        .collect(Collectors.toList()));
                        callbackContext.callback(this, pokemon);
                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage());
                        e.printStackTrace();
                        callbackContext.timedOut(this);
                    }
                });
    }

    @Override
    public void callback(Object caller, Object result) {
        callbackContext.callback(this, result);
    }

    @Override
    public void timedOut(Object caller) {
        callbackContext.timedOut(this);
    }
}
