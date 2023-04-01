package app.async_tasks.database;

import android.os.AsyncTask;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import app.async_tasks.TaskHelper;
import app.data_objects.Pokemon;
import app.ui.fragments.PokemonCollection;

public class GetPokemonList extends AsyncTask<List<Pokemon>, String, List<Pokemon>> {
    private final ICallbackContext callbackContext;

    public GetPokemonList(PokemonCollection callbackContext) {
        this.callbackContext = callbackContext;
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected List<Pokemon> doInBackground(List<Pokemon>... params) {
        List<Pokemon> pokemonList = params[0];

        List<Thread> threads = new ArrayList<>();
        pokemonList.forEach(pokemon -> threads.add(new Thread(() -> {
            TaskHelper.getPokemonData(pokemon);
            pokemon.setAbility(TaskHelper.getPokemonAbility(pokemon.getAbility().getName()));
            pokemon.setMoves(Arrays.asList(
                    TaskHelper.getMove(pokemon.getMoves().get(0).getName()),
                    TaskHelper.getMove(pokemon.getMoves().get(1).getName()),
                    TaskHelper.getMove(pokemon.getMoves().get(2).getName()),
                    TaskHelper.getMove(pokemon.getMoves().get(3).getName())
            ));
        })));

        threads.forEach(Thread::start);
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