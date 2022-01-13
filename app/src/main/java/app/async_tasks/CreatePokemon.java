package app.async_tasks;

import android.os.AsyncTask;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import app.Storage;
import app.data_objects.Move;
import app.data_objects.Nature;
import app.data_objects.Pokemon;
import app.fragments.PokemonCollection;

import java.util.ArrayList;

public class CreatePokemon extends AsyncTask {
    Fragment fragment;
    @RequiresApi(api = Build.VERSION_CODES.R)
    protected Object doInBackground(Object[] objects) {
        fragment = (Fragment) objects[0];
        String[] pokemonData = ((String) objects[1]).split(";");
        Pokemon pokemon = new Pokemon(
                -1,
                pokemonData[0],
                pokemonData[1],
                pokemonData[2],
                Integer.parseInt(pokemonData[3]),
                TaskHelper.getPokemonAbility(pokemonData[4]),
                Nature.getNature(pokemonData[5]),
                new ArrayList<Integer>() {{
                    add(Integer.parseInt(pokemonData[10]));
                    add(Integer.parseInt(pokemonData[11]));
                    add(Integer.parseInt(pokemonData[12]));
                    add(Integer.parseInt(pokemonData[13]));
                    add(Integer.parseInt(pokemonData[14]));
                    add(Integer.parseInt(pokemonData[15]));
                }},
                new ArrayList<Integer>() {{
                    add(Integer.parseInt(pokemonData[16]));
                    add(Integer.parseInt(pokemonData[17]));
                    add(Integer.parseInt(pokemonData[18]));
                    add(Integer.parseInt(pokemonData[19]));
                    add(Integer.parseInt(pokemonData[20]));
                    add(Integer.parseInt(pokemonData[21]));
                }},
                new ArrayList<Move>() {{
                    if (!pokemonData[6].equals("")) add(TaskHelper.getMove(pokemonData[6]));
                    if (!pokemonData[7].equals("")) add(TaskHelper.getMove(pokemonData[7]));
                    if (!pokemonData[8].equals("")) add(TaskHelper.getMove(pokemonData[8]));
                    if (!pokemonData[9].equals("")) add(TaskHelper.getMove(pokemonData[9]));
                }},
                TaskHelper.getPokemonTypes(pokemonData[1]),
                TaskHelper.getPokemonBaseStats(pokemonData[1]),
                null,
                TaskHelper.getPokemonSprite(pokemonData[1])
        );
        Storage.setSelectedPokemonForAdd(pokemon);
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        ((PokemonCollection)fragment).afterANewPokemonWasCreatedInScan();
    }
}
