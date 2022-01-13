package app.async_tasks.database;

import android.os.AsyncTask;
import android.os.Build;
import android.widget.BaseAdapter;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import app.Storage;
import app.async_tasks.TaskHelper;
import app.constants.StringConstants;
import app.data_objects.GridViewCell;
import app.data_objects.Move;
import app.data_objects.Nature;
import app.data_objects.Pokemon;
import app.fragments.PokemonCollection;
import app.layout_adapters.PokemonConfigurationAdapter;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Objects;

public class GetPokemonList extends AsyncTask implements StringConstants {
    private Fragment fragment;
    private final ArrayList<Pokemon> pokemonList = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.R)
    private void readFromDatabase() {
        try {
            String data = encodeStrings(
                    new String[]{"username"},
                    new String[]{Storage.getUsername()}
            );

            URL url = new URL(GET_POKEMON_FROM_DATABASE_LINK);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);

            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(data);
            wr.flush();

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            ArrayList<String> lines = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
                pokemonList.add(null);
            }
            ArrayList<Thread> threads = new ArrayList<>();

            for (String l : lines) {
                if (isCancelled()) break;
                Thread thread = new Thread(() -> {
                    String[] pokemonData = Objects.requireNonNull(l).split(";");
                    pokemonList.set(lines.indexOf(l), new Pokemon(
                            Integer.parseInt(pokemonData[0]),
                            pokemonData[1],
                            pokemonData[2],
                            pokemonData[3],
                            Integer.parseInt(pokemonData[4]),
                            TaskHelper.getPokemonAbility(pokemonData[5]),
                            Nature.getNature(pokemonData[6]),
                            new ArrayList<Integer>() {{
                                add(Integer.parseInt(pokemonData[11]));
                                add(Integer.parseInt(pokemonData[12]));
                                add(Integer.parseInt(pokemonData[13]));
                                add(Integer.parseInt(pokemonData[14]));
                                add(Integer.parseInt(pokemonData[15]));
                                add(Integer.parseInt(pokemonData[16]));
                            }},
                            new ArrayList<Integer>() {{
                                add(Integer.parseInt(pokemonData[17]));
                                add(Integer.parseInt(pokemonData[18]));
                                add(Integer.parseInt(pokemonData[19]));
                                add(Integer.parseInt(pokemonData[20]));
                                add(Integer.parseInt(pokemonData[21]));
                                add(Integer.parseInt(pokemonData[22]));
                            }},
                            new ArrayList<Move>() {{
                                if (!pokemonData[7].equals(""))
                                    add(TaskHelper.getMove(pokemonData[7]));
                                if (!pokemonData[8].equals(""))
                                    add(TaskHelper.getMove(pokemonData[8]));
                                if (!pokemonData[9].equals(""))
                                    add(TaskHelper.getMove(pokemonData[9]));
                                if (!pokemonData[10].equals(""))
                                    add(TaskHelper.getMove(pokemonData[10]));
                            }},
                            TaskHelper.getPokemonTypes(pokemonData[2]),
                            TaskHelper.getPokemonBaseStats(pokemonData[2]),
                            TaskHelper.getPokemonOfficialArt(pokemonData[2]),
                            TaskHelper.getPokemonSprite(pokemonData[2])
                    ));
                });
                thread.start();
                threads.add(thread);
            }
            threads.forEach(thread -> {
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected Object doInBackground(Object[] objects) {
        fragment = (Fragment) objects[0];
        readFromDatabase();
        ArrayList<GridViewCell> gridViewCells = new ArrayList<>();
        for (Pokemon pokemon : pokemonList) {
            gridViewCells.add(new GridViewCell(
                    pokemon.getID(),
                    pokemon.getOfficialArt(),
                    pokemon.getName(),
                    pokemon.getSpecies(),
                    pokemon.getTypes()
            ));
            if (isCancelled()) break;
        }
        return new PokemonConfigurationAdapter(fragment.getContext(), gridViewCells);
    }

    @Override
    protected void onPostExecute(Object object) {
        if (isCancelled()) return;
        ((PokemonCollection) fragment).setCollectionGridViewAdapter((BaseAdapter) object);
        ((PokemonCollection) fragment).setPokemonList(pokemonList);
    }
}