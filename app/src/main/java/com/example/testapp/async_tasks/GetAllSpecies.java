package com.example.testapp.async_tasks;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.testapp.PokemonConstants;
import com.example.testapp.StringConstants;
import com.example.testapp.data_objects.TYPE;
import com.example.testapp.fragments.AddPokemon;
import com.example.testapp.data_objects.SpeciesRow;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class GetAllSpecies extends AsyncTask implements PokemonConstants, StringConstants {
    private Fragment fragment;

    @RequiresApi(api = Build.VERSION_CODES.R)
    protected Object doInBackground(Object[] objects) {
        this.fragment = (Fragment) objects[0];
        ArrayList<SpeciesRow> speciesRows = new ArrayList<>();
        Document doc = null;
        try {
            doc = Jsoup.connect(ALL_POKEMON_LINK).get();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        assert doc != null;
        Elements elements = doc.getElementsByClass("ent-name");
        boolean already_exists;
        for (Element e : elements) {
            if (Integer.parseInt(e.parent().parent().getElementsByClass("infocard-cell-data").text()) > POKEDEX_NUMBER_LIMIT)
                break;
            already_exists = false;
            for (SpeciesRow speciesRow : speciesRows) {
                if (speciesRow.getSpecies().equals(e.text())) {
                    already_exists = true;
                    break;
                }
            }
            if (!already_exists) {
                Bitmap sprite = Helper.getPokemonSprite(e.text());
                String species = e.text();
                ArrayList<TYPE> types = Helper.getPokemonTypes(e);
                speciesRows.add(new SpeciesRow(sprite, species, types));
            }
        }
        return speciesRows;
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onPostExecute(Object object) {
        ((AddPokemon) fragment).setSpeciesRows((ArrayList<SpeciesRow>) object);
    }
}
