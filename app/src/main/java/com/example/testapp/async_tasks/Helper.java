package com.example.testapp.async_tasks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.testapp.PokemonConstants;
import com.example.testapp.StringConstants;
import com.example.testapp.data_objects.Ability;
import com.example.testapp.data_objects.Move;

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

public class Helper extends AsyncTask implements PokemonConstants, StringConstants {
    @Override
    protected Object doInBackground(Object[] objects) {
        return null;
    }

    public static ArrayList<Double> createArrayListOfDouble(double[] list) {
        ArrayList<Double> arrayList = new ArrayList<>();
        for (double v : list) {
            arrayList.add(v);
        }
        return arrayList;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static ArrayList<Integer> calculateStats(ArrayList<Integer> base, ArrayList<Integer> ivs,
                                                    ArrayList<Integer> evs, int level, ArrayList<Double> nature) {
        ArrayList<Integer> total = new ArrayList<>(base);
        for (int i = 0; i < total.size(); i++) {
            total.set(i, (int) (i == 0 ?
                    (2 * total.get(i)
                            + ivs.get(total.indexOf(total.get(i)))
                            + evs.get(total.indexOf(total.get(i))) / 4.0
                    ) * level / 100 + level + 10
                    :
                    ((2 * total.get(i)
                            + ivs.get(total.indexOf(total.get(i)))
                            + evs.get(total.indexOf(total.get(i))) / 4.0
                    ) * level / 100 + 5) * nature.get(i - 1)
            ));
        }
        return total;
    }

    public static Bitmap getBitmapImageFromElement(Element element) {
        try {
            String pokedexNumber = element.parent().parent().getElementsByClass("infocard-cell-data").get(0).text();
            String imageURL = StringConstants.getPokemonImageLink(pokedexNumber);
            URL url = new URL(imageURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    public static Bitmap getBitmapIconFromElement(Element element) {
        String s = element.text().toLowerCase();
        s = s.replace(FEMALE, "-f");
        s = s.replace(MALE, "-m");
        s = s.replace("'", "");
        s = s.replace(". ", "-");
        try {
            String imageURL = StringConstants.getPokemonIconLink(s);
            URL url = new URL(imageURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static ArrayList<String> getTypesFromElement(Element element) {
        return (ArrayList<String>) element.parent().parent().getElementsByClass("type-icon")
                .stream()
                .map(Element::text)
                .collect(Collectors.toList());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static Bitmap getBitmap(String s) {
        try {
            Document doc = Jsoup.connect(ALL_POKEMON_LINK).get();
            Elements elements = doc.getElementsByClass("ent-name");
            for (Element e : elements) {
                if (e.text().equals(s)) {
                    String pokedexNumber = e.parent().parent().getElementsByClass("infocard-cell-data").get(0).text();
                    String imageURL = StringConstants.getPokemonImageLink(pokedexNumber);
                    URL url = new URL(imageURL);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    InputStream input = connection.getInputStream();
                    return BitmapFactory.decodeStream(input);
                }
            }
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static ArrayList<String> getTypes(String s) {
        Document doc = null;
        try {
            doc = Jsoup.connect(ALL_POKEMON_LINK).get();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        assert doc != null;
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

    public static Ability getAbility(String s) {
        Document doc = null;
        try {
            doc = Jsoup.connect(ALL_ABILITIES_LINK).get();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        assert doc != null;
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
    public static ArrayList<Integer> getBaseStats(String s) {
        Document doc = null;
        try {
            doc = Jsoup.connect(ALL_POKEMON_LINK).get();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        assert doc != null;
        Elements elements = doc.getElementsByClass("ent-name");
        for (Element e : elements) {
            if (e.text().equals(s)) {
                ArrayList<Integer> ar = (ArrayList<Integer>) e.parent().parent().getElementsByClass("cell-num")
                        .stream().map(bs -> Integer.parseInt(bs.text())).collect(Collectors.toList());
                ar.remove(0);
                return ar;
            }
        }
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    public static ArrayList<Ability> getAbilitiesFromString(String pokemonName) {
        ArrayList<Ability> abilitiesRows = new ArrayList<>();
        String formatedPokemonName = pokemonName.toLowerCase()
                .replace(FEMALE, "-f")
                .replace(MALE, "-m")
                .replace("'", "")
                .replace(". ", "-");
        Document doc = null;
        try {
            doc = Jsoup.connect(StringConstants.getPokemonPageLink(formatedPokemonName)).get();
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        assert doc != null;
        Elements elements = doc.select("a[href^=/ability/]");
        for (Element e : elements) {
            abilitiesRows.add(new Ability(e.text(), e.attr("title")));
        }
        return abilitiesRows;
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    public static ArrayList<Move> getMovesFromString(String pokemonName) {
        ArrayList<Move> moves = new ArrayList<>();
        String formatedPokemonName = pokemonName.toLowerCase()
                .replace(FEMALE, "-f")
                .replace(MALE, "-m")
                .replace("'", "")
                .replace(". ", "-");
        Document doc = null;
        try {
            doc = Jsoup.connect(StringConstants.getPokemonMovesLink(formatedPokemonName)).get();
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        assert doc != null;

        Elements elements = doc.select("a[href^=/move/]");
        for (Element e : elements) {
            boolean already_exists = false;
            for (Move move : moves) {
                if (move.getName().equals(e.text()))
                    already_exists = true;
                break;
            }
            if (!already_exists) {
                String s = e.text().toLowerCase().replace(" ", "-");
                try {
                    doc = Jsoup.connect(StringConstants.getPokemonMoveLink(s)).get();
                } catch (IOException | NullPointerException e1) {
                    e1.printStackTrace();
                    System.exit(-1);
                }

                String name = doc.select("h1").text().split(" ")[0];

                //format them correctly
                Element element = doc.select("a[href^=/type/]").get(1);
                String type = element.text();
                String category = element.parent().parent().parent().child(1).child(1).text();
                String stringPower = element.parent().parent().parent().child(2).child(1).text();
                int power;
                if (stringPower.equals("—")) {
                    power = 0;
                } else {
                    power = Integer.parseInt(stringPower);
                }
                String stringAccuracy = element.parent().parent().parent().child(3).child(1).text();
                int accuracy;
                if (stringAccuracy.equals("∞") || stringAccuracy.equals("—")) {
                    accuracy = 100;
                } else {
                    accuracy = Integer.parseInt(stringAccuracy);
                }
                String stringPP = element.parent().parent().parent().child(4).child(1).text();
                int PP = Integer.parseInt(stringPP.split(" ")[0]);
                String description;
                if (doc.getElementsByClass("sun").size() == 1) {
                    description = doc.getElementsByClass("sun").get(0).parent().parent().child(1).text();
                } else {
                    description = doc.getElementsByClass("sun").get(1).parent().parent().child(1).text();
                }
                moves.add(new Move(name, type, category, power, accuracy, PP, description));
            }
        }
        return moves;
    }
}
