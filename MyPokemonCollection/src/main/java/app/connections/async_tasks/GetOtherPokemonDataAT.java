package app.connections.async_tasks;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.stream.Collectors;

import app.data_objects.Pokemon;
import app.ui.fragments.ICallbackContext;
import app.connections.web_scrapping.GetAbility;
import app.connections.web_scrapping.GetMove;
import app.connections.web_scrapping.GetPokemonBaseStats;

public class GetOtherPokemonDataAT extends GeneralisedTask<Pokemon> {

    private final Pokemon pokemon;

    public GetOtherPokemonDataAT(ICallbackContext callbackContext, Pokemon pokemon) {
        super(callbackContext);
        this.pokemon = pokemon;
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected Pokemon doInBackground(Void... voids) {
        pokemon.setAbility(GetAbility.get(pokemon.getAbility().getName()));
        pokemon.setMoves(pokemon.getMoves().stream().map(move -> GetMove.get(move.getName())).collect(Collectors.toList()));
        pokemon.setBaseStats(GetPokemonBaseStats.get(pokemon.getPokedexNumber()));
        return pokemon;
    }
}
