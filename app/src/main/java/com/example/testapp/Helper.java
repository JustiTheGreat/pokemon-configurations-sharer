package com.example.testapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.testapp.activities.PokemonDetailsActivity;
import com.example.testapp.data_objects.Ability;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class Helper extends AsyncTask implements PokemonConstants {
    private Fragment fragment;

    public Helper(Fragment fragment) {
        this.fragment = fragment;
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected Object doInBackground(Object[] objects) {
        return new ArrayList<Object>() {{
            try {
                add(getBitmap((String) objects[0]));
                add(getTypes((String) objects[0]));
                add(getAbility((String) objects[1]));
                add(getBaseStats((String) objects[0]));
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(-1);
            }
        }};
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onPostExecute(Object o) {
        ArrayList<Object> objects = (ArrayList<Object>) o;
        ((PokemonDetailsActivity) fragment).set(
                (Bitmap) objects.get(0),
                (ArrayList<String>) objects.get(1),
                (Ability) objects.get(2),
                (ArrayList<Integer>) objects.get(3)
        );
    }

    public static ArrayList<Double> createArrayListOfDouble(double[] list) {
        ArrayList<Double> arrayList = new ArrayList<>();
        for (double v : list) {
            arrayList.add(v);
        }
        return arrayList;
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    public static ArrayList<Double> getNature(String s) {
        for (NATURE N : NATURES) {
            if (s.equals(N.getName())) return N.getNature();
        }
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static ArrayList<Integer> calculateStats(ArrayList<Integer> base, ArrayList<Integer> ivs,
                                                    ArrayList<Integer> evs, int level, ArrayList<Double> nature) {
        ArrayList<Integer> total = new ArrayList<>(base);
        for (int i = 0; i < total.size(); i++) {
                total.set(i,(int) (i == 0 ?
                        (2 * total.get(i)
                                + ivs.get(total.indexOf(total.get(i)))
                                + evs.get(total.indexOf(total.get(i))) / 4.0
                        ) * level / 100 + level + 10
                        :
                        ((2 * total.get(i)
                                + ivs.get(total.indexOf(total.get(i)))
                                + evs.get(total.indexOf(total.get(i))) / 4.0
                        ) * level / 100 + 5) * nature.get(i-1)
                ));
            }
        return total;
    }

    public static Bitmap getBitmapFromElement(Element element) throws IOException {
        String pokedexNumber = element.parent().parent().getElementsByClass("infocard-cell-data").get(0).text();
        String imageURL = "https://assets.pokemon.com/assets/cms2/img/pokedex/full/" + pokedexNumber + ".png";
        URL url = new URL(imageURL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoInput(true);
        connection.connect();
        InputStream input = connection.getInputStream();
        return BitmapFactory.decodeStream(input);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static ArrayList<String> getTypesFromElement(Element element) throws IOException {
        return (ArrayList<String>) element.parent().parent().getElementsByClass("type-icon")
                .stream()
                .map(Element::text)
                .collect(Collectors.toList());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public Bitmap getBitmap(String s) throws IOException {
        Document doc = Jsoup.connect("https://pokemondb.net/pokedex/all").get();
        Elements elements = doc.getElementsByClass("ent-name");
        for (Element e : elements) {
            if (e.text().equals(s)) {
                String pokedexNumber = e.parent().parent().getElementsByClass("infocard-cell-data").get(0).text();
                String imageURL = "https://assets.pokemon.com/assets/cms2/img/pokedex/full/" + pokedexNumber + ".png";
                URL url = new URL(imageURL);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                return BitmapFactory.decodeStream(input);
            }
        }
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public ArrayList<String> getTypes(String s) throws IOException {
        Document doc = Jsoup.connect("https://pokemondb.net/pokedex/all").get();
        Elements elements = doc.getElementsByClass("ent-name");
        for (Element e : elements) {
            if (e.text().equals(s)) {
                return (ArrayList<String>) e.parent().parent().getElementsByClass("type-icon")
                        .stream()
                        .map(Element::text)
                        .collect(Collectors.toList());
            }
        }
        return null;
    }

    public Ability getAbility(String s) throws IOException {
        Document doc = Jsoup.connect("https://pokemondb.net/ability").get();
        Elements elements = doc.getElementsByClass("ent-name");
        for (Element e : elements) {
            if (e.text().equals(s)) {
                return new Ability(
                        e.text(),
                        e.parent().parent().getElementsByClass("cell-med-text").get(0).text()
                );
            }
        }
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public ArrayList<Integer> getBaseStats(String s) throws IOException {
        Document doc = Jsoup.connect("https://pokemondb.net/pokedex/all").get();
        Elements elements = doc.getElementsByClass("ent-name");
        for (Element e : elements) {
            if (e.text().equals(s)) {
                ArrayList<Integer> ar= (ArrayList<Integer>) e.parent().parent().getElementsByClass("cell-num")
                        .stream().map(bs -> Integer.parseInt(bs.text())).collect(Collectors.toList());
                ar.remove(0);
                return ar;
            }
        }
        return null;
    }
}
