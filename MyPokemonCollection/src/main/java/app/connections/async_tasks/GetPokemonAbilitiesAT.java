package app.connections.async_tasks;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.List;

import app.data_objects.Ability;
import app.ui.fragments.ICallbackContext;
import app.connections.web_scrapping.GetPokemonAbilities;

public class GetPokemonAbilitiesAT extends GeneralisedTask<List<Ability>> {

    private final long pokedexNumber;

    public GetPokemonAbilitiesAT(ICallbackContext callbackContext, long pokedexNumber) {
        super(callbackContext);
        this.pokedexNumber = pokedexNumber;
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected List<Ability> doInBackground(Void... voids) {
        return GetPokemonAbilities.get(pokedexNumber);
    }
}