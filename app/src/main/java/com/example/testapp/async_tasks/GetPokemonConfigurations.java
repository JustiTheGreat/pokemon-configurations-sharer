package com.example.testapp.async_tasks;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.widget.BaseAdapter;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.testapp.LoggedUser;
import com.example.testapp.PokemonConstants;
import com.example.testapp.StringConstants;
import com.example.testapp.data_objects.TYPE;
import com.example.testapp.fragments.PokemonCollection;
import com.example.testapp.data_objects.GridViewCell;
import com.example.testapp.data_objects.PokemonConfiguration;
import com.example.testapp.layout_adapters.PokemonConfigurationAdapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class GetPokemonConfigurations extends AsyncTask implements PokemonConstants, StringConstants {
    private Fragment fragment;
    private ArrayList<PokemonConfiguration> pokemonConfigurations;

    @RequiresApi(api = Build.VERSION_CODES.N)
    private ArrayList<PokemonConfiguration> readFromDatabase() {
        ArrayList<PokemonConfiguration> pokemonConfigurations = new ArrayList<>();
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
                String[] configuration = line.split(":");
                pokemonConfigurations.add(new PokemonConfiguration(
                        Integer.parseInt(configuration[0]),
                        configuration[1],
                        configuration[2],
                        configuration[3],
                        configuration[4],
                        configuration[5],
                        Integer.parseInt(configuration[6]),
                        new ArrayList<Integer>() {{
                            add(Integer.parseInt(configuration[7]));
                            add(Integer.parseInt(configuration[8]));
                            add(Integer.parseInt(configuration[9]));
                            add(Integer.parseInt(configuration[10]));
                            add(Integer.parseInt(configuration[11]));
                            add(Integer.parseInt(configuration[12]));
                        }},
                        new ArrayList<Integer>() {{
                            add(Integer.parseInt(configuration[13]));
                            add(Integer.parseInt(configuration[14]));
                            add(Integer.parseInt(configuration[15]));
                            add(Integer.parseInt(configuration[16]));
                            add(Integer.parseInt(configuration[17]));
                            add(Integer.parseInt(configuration[18]));
                        }},
                        null
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
        return pokemonConfigurations;
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected Object doInBackground(Object[] objects) {
        this.fragment = (Fragment) objects[0];
        ArrayList<GridViewCell> gridViewCells = new ArrayList<>();
        pokemonConfigurations = readFromDatabase();
        Document doc = null;
        try {
            doc = Jsoup.connect(ALL_POKEMON_LINK).get();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        assert doc != null;
        Elements elements = doc.getElementsByClass("ent-name");
        pokemonConfigurations.forEach(pc -> {
            for (Element e : elements) {
                if (Integer.parseInt(e.parent().parent().getElementsByClass("infocard-cell-data").text()) > POKEDEX_NUMBER_LIMIT)
                    break;
                if (e.text().equals(pc.getSpecies())) {
                    long id = pc.getId();
                    Bitmap image = Helper.getBitmapImageFromElement(e);
                    String species = pc.getSpecies();
                    String name = pc.getName();
                    ArrayList<TYPE> types = (ArrayList<TYPE>) Helper.getTypesFromElement(e)
                            .stream().map(t->TYPE.getType(t)).collect(Collectors.toList());
                    gridViewCells.add(new GridViewCell(id, image, species, name, types));
                    break;
                }
            }
        });
        return new PokemonConfigurationAdapter(fragment.getContext(), gridViewCells);
    }

    @Override
    protected void onPostExecute(Object object) {
        ((PokemonCollection) fragment).setGridViewAdapter((BaseAdapter) object);
        ((PokemonCollection) fragment).setPokemonConfigurations(pokemonConfigurations);
    }
}