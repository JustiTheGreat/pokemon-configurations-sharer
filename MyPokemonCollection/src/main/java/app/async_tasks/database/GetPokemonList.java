package app.async_tasks.database;

import static android.content.ContentValues.TAG;
import static app.constants.StringConstants.GET_POKEMON_FROM_DATABASE_LINK;
import static app.constants.StringConstants.LOGIN_PROBLEMS;
import static app.constants.StringConstants.LOGIN_SUCCESS;
import static app.constants.StringConstants.encodeStrings;
import static app.constants.collection_fields.Pokemon.ABILITY;
import static app.constants.collection_fields.Pokemon.ATTACK;
import static app.constants.collection_fields.Pokemon.EVS;
import static app.constants.collection_fields.Pokemon.GENDER;
import static app.constants.collection_fields.Pokemon.HIT_POINTS;
import static app.constants.collection_fields.Pokemon.IVS;
import static app.constants.collection_fields.Pokemon.LEVEL;
import static app.constants.collection_fields.Pokemon.MOVE1;
import static app.constants.collection_fields.Pokemon.MOVE2;
import static app.constants.collection_fields.Pokemon.MOVE3;
import static app.constants.collection_fields.Pokemon.MOVE4;
import static app.constants.collection_fields.Pokemon.NAME;
import static app.constants.collection_fields.Pokemon.NATURE;
import static app.constants.collection_fields.Pokemon.SPECIES;
import static app.constants.collection_fields.Pokemon.UID;
import static app.constants.collection_fields.Users.PASSWORD;
import static app.constants.collection_fields.Users.POKEMON_LIST;
import static app.constants.collection_fields.Users.USERNAME;

import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.navigation.fragment.NavHostFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;
import com.mypokemoncollection.R;

import org.w3c.dom.Document;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;


import app.async_tasks.TaskHelper;
import app.data_objects.Move;
import app.data_objects.Nature;
import app.data_objects.Pokemon;

public class GetPokemonList extends AsyncTask<String, String, List<Pokemon>> {
    private final ICallbackContext callbackContext;

    public GetPokemonList(ICallbackContext callbackContext) {
        this.callbackContext = callbackContext;
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected List<Pokemon> doInBackground(String... params) {
        List<Pokemon> pokemonList = new ArrayList<>();
        //AtomicReference<List<String>> pokemonPaths = new AtomicReference<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            return pokemonList;
        }

        String user_id = currentUser.getUid();
        List<Thread> threads = new ArrayList<>();
        db.collection("pokemon")
                .whereEqualTo(UID, user_id)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<DocumentSnapshot> documents = task.getResult().getDocuments();
                        documents.forEach(document -> {
                            pokemonList.add(null);
                            Thread thread = new Thread(() -> pokemonList.set(documents.indexOf(document),
                                    new Pokemon(
                                            document.getId(),
                                            document.getString(NAME),
                                            document.getString(SPECIES),
                                            document.getString(GENDER),
                                            Math.toIntExact(document.getLong(LEVEL)),
                                            TaskHelper.getPokemonAbility(document.getString(ABILITY)),
                                            Nature.getNature(document.getString(NATURE)),
                                            ((Map<String, Integer>) document.get(IVS)).values().stream().collect(Collectors.toList()),
                                            ((Map<String, Integer>) document.get(EVS)).values().stream().collect(Collectors.toList()),
                                            new ArrayList<Move>() {{
                                                add(TaskHelper.getMove(document.getString(MOVE1)));
                                                add(TaskHelper.getMove(document.getString(MOVE2)));
                                                add(TaskHelper.getMove(document.getString(MOVE3)));
                                                add(TaskHelper.getMove(document.getString(MOVE4)));
                                            }},
                                            TaskHelper.getPokemonTypes(document.getString(SPECIES)),
                                            TaskHelper.getPokemonBaseStats(document.getString(SPECIES)),
                                            TaskHelper.getPokemonOfficialArt(document.getString(SPECIES)),
                                            TaskHelper.getPokemonSprite(document.getString(SPECIES))
                                    )));
                            thread.start();
                            threads.add(thread);
                        });
//                        for (QueryDocumentSnapshot document : task.getResult()) {
//                            pokemonList.add(null);
//                            Thread thread = new Thread(() -> pokemonList.set(pokemonPaths.get().indexOf(pokemonPath),
//                                    new Pokemon(
//                                            document.getId(),
//                                            documentSnapshot.getString(NAME),
//                                            documentSnapshot.getString(SPECIES),
//                                            documentSnapshot.getString(GENDER),
//                                            Integer.parseInt(documentSnapshot.getString(LEVEL)),
//                                            TaskHelper.getPokemonAbility(documentSnapshot.getString(ABILITY)),
//                                            Nature.getNature(documentSnapshot.getString(NATURE)),
//                                            ((Map<String, Integer>) documentSnapshot.get(IVS)).values().stream().collect(Collectors.toList()),
//                                            ((Map<String, Integer>) documentSnapshot.get(EVS)).values().stream().collect(Collectors.toList()),
//                                            new ArrayList<Move>() {{
//                                                add(TaskHelper.getMove(documentSnapshot.getString(MOVE1)));
//                                                add(TaskHelper.getMove(documentSnapshot.getString(MOVE2)));
//                                                add(TaskHelper.getMove(documentSnapshot.getString(MOVE3)));
//                                                add(TaskHelper.getMove(documentSnapshot.getString(MOVE4)));
//                                            }},
//                                            TaskHelper.getPokemonTypes(documentSnapshot.getString(SPECIES)),
//                                            TaskHelper.getPokemonBaseStats(documentSnapshot.getString(SPECIES)),
//                                            TaskHelper.getPokemonOfficialArt(documentSnapshot.getString(SPECIES)),
//                                            TaskHelper.getPokemonSprite(documentSnapshot.getString(SPECIES))
//                                    )));
//                            thread.start();
//                            threads.add(thread);


//                            pokemonPaths.set((List<String>) document.get(POKEMON_LIST));
//                            Log.d(TAG, document.getId() + " => " + document.getData());
//                        }
                    } else {
                        Log.w(TAG, "Error getting documents.", task.getException());
                    }
                });

        threads.forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        return pokemonList;
    }

    @Override
    protected void onPostExecute(List<Pokemon> result) {
        if (isCancelled()) return;
        if (result == null) callbackContext.timedOut();
        else callbackContext.callback(this, result);
    }
}