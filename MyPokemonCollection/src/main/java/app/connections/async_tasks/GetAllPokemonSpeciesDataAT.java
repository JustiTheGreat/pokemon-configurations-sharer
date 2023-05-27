package app.connections.async_tasks;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.List;

import app.data_objects.Pokemon;
import app.connections.firebase.GetPokemonSpriteDB;
import app.ui.fragments.ICallbackContext;
import app.connections.web_scrapping.GetAllPokemonSpeciesData;

public class GetAllPokemonSpeciesDataAT extends GeneralisedTask<List<Pokemon>> implements ICallbackContext {

    private long targetNumberOfResponses;
    private long responses = 0;

    public GetAllPokemonSpeciesDataAT(ICallbackContext callbackContext) {
        super(callbackContext);
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected List<Pokemon> doInBackground(Void... voids) {
        List<Pokemon> pokemonList = GetAllPokemonSpeciesData.get();
        targetNumberOfResponses = pokemonList.size();
        pokemonList.forEach(pokemon -> new GetPokemonSpriteDB(this, pokemon).execute());
        while (responses != targetNumberOfResponses) ;
        return pokemonList;
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
