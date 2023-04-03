package app.async_tasks.web_scraping;

import android.os.AsyncTask;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import app.async_tasks.TaskHelper;
import app.data_objects.Pokemon;
import app.ui.fragments.ICallbackContext;

public class GetPokemonList extends AsyncTask<List<Pokemon>, String, List<Pokemon>> {
    private final ICallbackContext callbackContext;

    public GetPokemonList(ICallbackContext callbackContext) {
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
            pokemon.setMoves(pokemon.getMoves().stream().map(move->TaskHelper.getMove(move.getName())).collect(Collectors.toList()));
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