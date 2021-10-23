package com.example.testapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.testapp.data_objects.PokemonConfiguration;

import org.jsoup.nodes.Element;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class Helper implements HelperInterface {
    public String getPokedexNumber(Element element) {
        return element.parent().parent().getElementsByClass("infocard-cell-data").get(0).text();
    }

    public String getImageURL(Element element) {
        return "https://assets.pokemon.com/assets/cms2/img/pokedex/full/" + getPokedexNumber(element) + ".png";
    }

    public Bitmap getImageViewFromElement(Element element) {
        try {
            URL url = new URL(getImageURL(element));
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public ArrayList<String> getTypesFromElement(Element element) {
        return (ArrayList<String>) element.parent().parent().getElementsByClass("type-icon").stream()
                .map(Element::text)
                .collect(Collectors.toList());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public ArrayList<Integer> getBaseStatsFromElement(Element element) {
        return (ArrayList<Integer>) element.parent().parent().getElementsByClass("type-num").stream()
                .map(bs -> Integer.parseInt(bs.text())).collect(Collectors.toList());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public ArrayList<Integer> calculateStats(Element e, PokemonConfiguration pc) {
        ArrayList<Integer> stats = getBaseStatsFromElement(e);
        stats.forEach(bs -> bs = (stats.indexOf(bs) == 0 ?
                        (
                                2 * bs
                                        + pc.getIVs().get(stats.indexOf(bs))
                                        + pc.getEVs().get(stats.indexOf(bs)) / 4
                        ) * pc.getLevel() / 100 + pc.getLevel() + 10
                        :
                        (
                                2 * bs
                                        + pc.getIVs().get(stats.indexOf(bs))
                                        + pc.getEVs().get(stats.indexOf(bs)) / 4
                        ) * pc.getLevel() / 100 + 5
                )
        );
        return stats;
    }
}
