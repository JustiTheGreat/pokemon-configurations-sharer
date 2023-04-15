package app.async_tasks;

import android.os.AsyncTask;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.List;

import app.web_scrapping.GetAllPokemonSpeciesData;
import app.firebase.GetPokemonSpriteDB;
import app.data_objects.Pokemon;
import app.ui.fragments.ICallbackContext;

public class GetAllPokemonSpeciesDataAT extends AsyncTask<String, String, List<Pokemon>> implements ICallbackContext {

    private final ICallbackContext callbackContext;
    private long targetNumberOfResponses;
    private long responses = 0;

    public GetAllPokemonSpeciesDataAT(ICallbackContext callbackContext) {
        this.callbackContext = callbackContext;
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected List<Pokemon> doInBackground(String... strings) {
        List<Pokemon> pokemonList = GetAllPokemonSpeciesData.get();
        targetNumberOfResponses = pokemonList.size();
        pokemonList.forEach(pokemon -> new GetPokemonSpriteDB(this, pokemon).execute());
        while (responses != targetNumberOfResponses) ;
        return pokemonList;
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onPostExecute(List<Pokemon> result) {
        if (isCancelled()) return;
        if (result == null) callbackContext.timedOut(this);
        else callbackContext.callback(this, result);
    }

    @Override
    public void callback(Object caller, Object result) {
        responses++;
    }

    @Override
    public void timedOut(Object caller) {
        responses = targetNumberOfResponses;
    }
}
