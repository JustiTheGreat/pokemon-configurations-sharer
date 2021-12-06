package com.example.testapp.async_tasks;

import android.os.AsyncTask;
import android.os.Build;
import android.widget.BaseAdapter;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.testapp.LoggedUser;
import com.example.testapp.StringConstants;
import com.example.testapp.data_objects.GridViewCell;
import com.example.testapp.data_objects.Move;
import com.example.testapp.data_objects.Nature;
import com.example.testapp.data_objects.Pokemon;
import com.example.testapp.fragments.PokemonCollection;
import com.example.testapp.layout_adapters.PokemonConfigurationAdapter;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class GetPokemonList extends AsyncTask implements StringConstants {
    private Fragment fragment;
    private final ArrayList<Pokemon> pokemonList = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.R)
    private void readFromDatabase() {
        try {
            String data = encodeStrings(
                    new String[]{"username"},
                    new String[]{LoggedUser.getUsername()}
            );

            URL url = new URL(GET_POKEMON_FROM_DATABASE_LINK);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);

            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(data);
            wr.flush();

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;

            while ((line = reader.readLine()) != null) {
                String[] pokemonData = line.split(";");
                pokemonList.add(new Pokemon(
                        Integer.parseInt(pokemonData[0]),
                        pokemonData[1],
                        pokemonData[2],
                        pokemonData[3],
                        Integer.parseInt(pokemonData[4]),
                        Helper.getPokemonAbility(pokemonData[5]),
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
                            if (pokemonData[7].equals("")) add(null);
                            else add(Helper.getMove(pokemonData[7]));
                            if (pokemonData[8].equals("")) add(null);
                            else add(Helper.getMove(pokemonData[8]));
                            if (pokemonData[9].equals("")) add(null);
                            else add(Helper.getMove(pokemonData[9]));
                            if (pokemonData[10].equals("")) add(null);
                            else add(Helper.getMove(pokemonData[1]));
                        }},
                        Helper.getPokemonTypes(pokemonData[2]),
                        Helper.getPokemonBaseStats(pokemonData[2]),
                        Helper.getPokemonOfficialArt(pokemonData[2]),
                        Helper.getPokemonSprite(pokemonData[2])
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected Object doInBackground(Object[] objects) {
        fragment = (Fragment) objects[0];
        readFromDatabase();
        ArrayList<GridViewCell> gridViewCells = new ArrayList<>();
        pokemonList.forEach(pokemon -> gridViewCells.add(new GridViewCell(
                pokemon.getID(),
                pokemon.getOfficialArt(),
                pokemon.getName(),
                pokemon.getSpecies(),
                pokemon.getTypes()
        )));
        return new PokemonConfigurationAdapter(fragment.getContext(), gridViewCells);
    }

    @Override
    protected void onPostExecute(Object object) {
        ((PokemonCollection) fragment).setCollectionGridViewAdapter((BaseAdapter) object);
        ((PokemonCollection) fragment).setPokemonList(pokemonList);
    }
}