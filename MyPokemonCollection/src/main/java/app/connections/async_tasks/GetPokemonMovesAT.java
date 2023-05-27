package app.connections.async_tasks;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.List;

import app.data_objects.Move;
import app.ui.fragments.ICallbackContext;
import app.connections.web_scrapping.GetPokemonMoves;

public class GetPokemonMovesAT extends GeneralisedTask<List<Move>> {

    private final long pokedexNumber;

    public GetPokemonMovesAT(ICallbackContext callbackContext, long pokedexNumber) {
        super(callbackContext);
        this.pokedexNumber = pokedexNumber;
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected List<Move> doInBackground(Void... voids) {
        return GetPokemonMoves.get(pokedexNumber);
    }
}
