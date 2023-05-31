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

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import app.data_objects.Pokemon;
import app.ui.fragments.ICallbackContext;

public class GetFilteredPokemonListPartialDisplayDataListDB implements ICallbackContext {

    private final ICallbackContext callbackContext;
    private final long pokedexNumber;
    private final int count;
    private final int BATCH_SIZE = 3;
    private static boolean canRead = true;

    public static boolean canRead() {
        return canRead;
    }

    public static void setCanRead(boolean canRead) {
        GetFilteredPokemonListPartialDisplayDataListDB.canRead = canRead;
    }

    public GetFilteredPokemonListPartialDisplayDataListDB(ICallbackContext callbackContext, long pokedexNumber, int count) {
        this.callbackContext = callbackContext;
        this.pokedexNumber = pokedexNumber;
        this.count = count;
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    public void execute() {
        CollectionReference collectionReference = FirebaseFirestore.getInstance().collection(PUBLIC_POKEMON_COLLECTION);
        Query query = pokedexNumber == DEFAULT_POKEDEX_NUMBER?collectionReference:collectionReference.whereEqualTo(POKEDEX_NUMBER, pokedexNumber);

        query.orderBy(NAME)
                .limit((long) count * BATCH_SIZE)
                .get()
                .addOnFailureListener(this::failureListener)
                .addOnSuccessListener(querySnapshot -> {
                    if (count == 3) {
                        successListener(querySnapshot);
                    } else {
                        int documentIndex = querySnapshot.size() - (querySnapshot.size() % BATCH_SIZE == 0 ? BATCH_SIZE : querySnapshot.size() % BATCH_SIZE);
                        DocumentSnapshot lastVisible = querySnapshot.getDocuments().get(documentIndex);
                        query.orderBy(NAME)
                                .startAt(lastVisible)
                                .limit(BATCH_SIZE)
                                .get()
                                .addOnFailureListener(this::failureListener)
                                .addOnSuccessListener(this::successListener);
                    }
                });
    }
    private void failureListener(Exception e) {
        Log.e(TAG, e.getMessage());
        e.printStackTrace();
        callbackContext.timedOut(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    private void successListener(QuerySnapshot querySnapshot){
        if (querySnapshot.isEmpty()) {
            canRead = false;
            return;
        }
        if (querySnapshot.size() < BATCH_SIZE
                || count == 3 && querySnapshot.size() < count * BATCH_SIZE) {
            canRead = false;
        }
        try {
            List<Pokemon> pokemonList = new ArrayList<>();
            List<DocumentSnapshot> documents = querySnapshot.getDocuments();
            for (DocumentSnapshot document : documents) {
                Pokemon pokemon = Pokemon.newPokemon()
                        .id(document.getId())
                        .name(document.getString(NAME))
                        .pokedexNumber(document.getLong(POKEDEX_NUMBER))
                        .gender(document.getString(GENDER))
                        .shiny(Boolean.TRUE.equals(document.getBoolean(SHINY)))
                        .userId(document.getString(USER_ID));
                pokemonList.add(pokemon);
                new GetPokemonHomeArtDB(this, pokemon).execute();
            }
            callbackContext.callback(this, pokemonList);
        } catch (Exception e) {
            failureListener(e);
        }
    }

    @Override
    public void callback(Object caller, Object result) {
    }

    @Override
    public void timedOut(Object caller) {
    }
}
