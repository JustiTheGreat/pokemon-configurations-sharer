package app.async_tasks;

import static app.constants.PokemonDatabaseFields.ABILITY;

import android.os.AsyncTask;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.stream.Collectors;

import app.data_objects.Pokemon;
import app.firebase.GetPokemonSpriteDB;
import app.ui.fragments.ICallbackContext;
import app.web_scrapping.GetAbility;
import app.web_scrapping.GetMove;
import app.web_scrapping.GetPokemonBaseStats;

public class GetOtherPokemonDataAT extends AsyncTask<Pokemon, String, Pokemon> {
    private final ICallbackContext callbackContext;

    public GetOtherPokemonDataAT(ICallbackContext callbackContext) {
        this.callbackContext = callbackContext;
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected Pokemon doInBackground(Pokemon... params) {
        Pokemon pokemon = params[0];
        pokemon.setAbility(GetAbility.get(pokemon.getAbility().getName()));
        pokemon.setMoves(pokemon.getMoves().stream().map(move -> GetMove.get(move.getName())).collect(Collectors.toList()));
        pokemon.setBaseStats(GetPokemonBaseStats.get(pokemon.getPokedexNumber()));
        return pokemon;
    }

    @Override
    protected void onPostExecute(Pokemon result) {
        if (isCancelled()) return;
        if (result == null) callbackContext.timedOut(this);
        else callbackContext.callback(this, result);
    }
}
