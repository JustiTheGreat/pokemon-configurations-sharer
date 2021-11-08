package com.example.testapp.communication;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.testapp.Helper;
import com.example.testapp.PokemonConstants;
import com.example.testapp.activities.AddPokemon;
import com.example.testapp.data_objects.SpeciesRow;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class GetSpecies extends AsyncTask implements PokemonConstants {
    private Fragment fragment;

    @RequiresApi(api = Build.VERSION_CODES.R)
    protected Object doInBackground(Object[] objects) {
        this.fragment = (Fragment) objects[0];
        ArrayList<SpeciesRow> speciesRows = new ArrayList<>();
        Document doc = null;
        try {
            doc = Jsoup.connect("https://pokemondb.net/pokedex/all").get();
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
                try {
                    Bitmap image = Helper.getBitmapIconFromElement(e);
                    String species = e.text();
                    ArrayList<String> types = Helper.getTypesFromElement(e);
                    speciesRows.add(new SpeciesRow(image, species, types));
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                    System.exit(-1);
                }
            }
        }
        return speciesRows;
    }

    @Override
    protected void onPostExecute(Object object) {
        ((AddPokemon) fragment).setSpeciesRows((ArrayList<SpeciesRow>) object);
    }
}
