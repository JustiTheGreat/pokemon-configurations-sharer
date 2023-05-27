package app.connections.firebase;

import static app.constants.PokemonDatabaseFields.GENDER;
import static app.constants.PokemonDatabaseFields.NAME;
import static app.constants.PokemonDatabaseFields.POKEDEX_NUMBER;
import static app.constants.PokemonDatabaseFields.SHINY;
import static app.constants.PokemonDatabaseFields.USER_ID;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

import app.data_objects.Pokemon;
import app.ui.fragments.ICallbackContext;

public class GetPokemonDisplayDataListDB implements ICallbackContext {

    private final ICallbackContext callbackContext;
    private final String userId;
    private final String collection;

    public GetPokemonDisplayDataListDB(ICallbackContext callbackContext, String userId, String collection) {
        this.callbackContext = callbackContext;
        this.userId = userId;
        this.collection = collection;
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    public void execute() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Query query;
        if (userId == null) query = db.collection(collection);
        else query = db.collection(collection).whereEqualTo(USER_ID, userId);

        query.get()
                .addOnFailureListener(task -> callbackContext.timedOut(this))
                .addOnSuccessListener(task -> {
                            List<Pokemon> pokemonList = new ArrayList<>();
                            List<DocumentSnapshot> documents = task.getDocuments();
                            for (DocumentSnapshot document : documents) {
                                Pokemon pokemon = Pokemon.newPokemon()
                                        .id(document.getId())
                                        .name(document.getString(NAME))
                                        .pokedexNumber(document.getLong(POKEDEX_NUMBER))
                                        .gender(document.getString(GENDER))
                                        .shiny(document.getBoolean(SHINY))
                                        .userId(userId);
                                new GetPokemonHomeArtDB(this, pokemon).execute();
                                pokemonList.add(pokemon);
                            }
                            callbackContext.callback(this, pokemonList);
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
