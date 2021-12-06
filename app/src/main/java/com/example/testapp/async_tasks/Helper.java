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
import com.example.testapp.data_objects.MoveCategory;
import com.example.testapp.data_objects.Nature;
import com.example.testapp.data_objects.TYPE;

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
                                                    ArrayList<Integer> evs, int level, Nature nature) {
        ArrayList<Integer> total = new ArrayList<>(base);
        ArrayList<Double> natureMultipliers = nature.getEffects();
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
                    ) * level / 100 + 5) * natureMultipliers.get(i - 1)
            ));
        }
        return total;
    }

    public static Ability getPokemonAbility(String s) {
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
    public static ArrayList<TYPE> getPokemonTypes(String s) {
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
                return (ArrayList<TYPE>) e.parent().parent().getElementsByClass("type-icon")
                        .stream()
                        .map(Element::text)
                        .map(TYPE::getType)
                        .collect(Collectors.toList());
            }
        }
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static ArrayList<TYPE> getPokemonTypes(Element element) {
        return (ArrayList<TYPE>) element.parent().parent().getElementsByClass("type-icon")
                .stream()
                .map(Element::text)
                .map(TYPE::getType)
                .collect(Collectors.toList());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static ArrayList<Integer> getPokemonBaseStats(String s) {
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
                ArrayList<Integer> baseStats = (ArrayList<Integer>) e.parent().parent().getElementsByClass("cell-num")
                        .stream().map(bs -> Integer.parseInt(bs.text())).collect(Collectors.toList());
                baseStats.remove(0);
                return baseStats;
            }
        }
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static Bitmap getPokemonOfficialArt(String s) {
        try {
            Document doc = Jsoup.connect(ALL_POKEMON_LINK).get();
            Elements elements = doc.getElementsByClass("ent-name");
            for (Element e : elements) {
                if (e.text().equals(s)) {
                    String pokedexNumber = e.parent().parent().getElementsByClass("infocard-cell-data").get(0).text();
                    URL url = new URL(POKEMON_OFFICIAL_ART_LINK.replace("?", pokedexNumber));
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

    @RequiresApi(api = Build.VERSION_CODES.R)
    public static Bitmap getPokemonSprite(String pokemonSpecies) {
        String s = pokemonSpecies.toLowerCase()
                .replace(FEMALE, "-f")
                .replace(MALE, "-m")
                .replace("'", "")
                .replace(". ", "-");
        try {
            URL url = new URL(POKEMON_SPRITE_LINK.replace("?", s));
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
    public static Move getMove(String moveName) {
        Document doc = null;
        try {
            doc = Jsoup.connect(MOVES_LINK).get();
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        assert doc != null;
        Elements elements = doc.select("a[href^=/move/]");
        for (Element e : elements) {
            if (e.text().equals(moveName)) {
                String s = e.text().toLowerCase().replace(" ", "-");
                try {
                    doc = Jsoup.connect(MOVE_LINK.replace("?", s)).get();
                } catch (IOException | NullPointerException e1) {
                    e1.printStackTrace();
                    System.exit(-1);
                }

                Element element = doc.select("a[href^=/type/]").get(1);

                TYPE type = TYPE.getType(element.text());

                String categoryName = element.parent().parent().parent().child(1).child(1).text().split(" ")[1].trim().toLowerCase();
                MoveCategory moveCategory = getMoveCategory(categoryName);

                String stringPower = element.parent().parent().parent().child(2).child(1).text();
                int power = stringPower.equals("—") ? 0 : Integer.parseInt(stringPower);

                String stringAccuracy = element.parent().parent().parent().child(3).child(1).text();
                int accuracy = stringAccuracy.equals("∞") || stringAccuracy.equals("—") ? 100 : Integer.parseInt(stringAccuracy);

                String stringPP = element.parent().parent().parent().child(4).child(1).text();
                int PP = Integer.parseInt(stringPP.split(" ")[0]);

                String description;
                if (doc.getElementsByClass("sun").size() == 1) {
                    description = doc.getElementsByClass("sun").get(0).parent().parent().child(1).text();
                } else {
                    description = doc.getElementsByClass("sun").get(1).parent().parent().child(1).text();
                }

                return new Move(moveName, type, moveCategory, power, accuracy, PP, description);
            }
        }
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    public static ArrayList<Ability> getPokemonAbilities(String pokemonName) {
        ArrayList<Ability> abilitiesRows = new ArrayList<>();
        String formatedPokemonName = pokemonName.toLowerCase()
                .replace(FEMALE, "-f")
                .replace(MALE, "-m")
                .replace("'", "")
                .replace(". ", "-");
        Document doc = null;
        try {
            doc = Jsoup.connect(POKEMON_PAGE_LINK.replace("?", formatedPokemonName)).get();
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
    public static ArrayList<Move> getPokemonMoves(String pokemonName) {
        String formattedPokemonName = pokemonName.toLowerCase()
                .replace(FEMALE, "-f")
                .replace(MALE, "-m")
                .replace("'", "")
                .replace(". ", "-");
        Document doc = null;
        try {
            doc = Jsoup.connect(POKEMON_MOVES_LINK.replace("?", formattedPokemonName)).get();
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        assert doc != null;
        Elements elements = doc.select("a[href^=/move/]");
        ArrayList<Move> moves = new ArrayList<>();
        for (Element e : elements) {
            String elementName = e.text();
            boolean already_exists = false;
            for (Move move : moves) {
                if (elementName.equals(move.getName())) {
                    already_exists = true;
                    break;
                }
            }
            if (!already_exists) {
                String s = elementName.toLowerCase().replace(" ", "-");
                try {
                    doc = Jsoup.connect(MOVE_LINK.replace("?", s)).get();
                } catch (IOException | NullPointerException e1) {
                    e1.printStackTrace();
                    System.exit(-1);
                }

                String name = doc.select("h1").text().replace(" (move)", "");

                Element element = doc.select("a[href^=/type/]").get(1);

                TYPE type = TYPE.getType(element.text());

                String categoryName = element.parent().parent().parent().child(1).child(1).text().split(" ")[1].trim().toLowerCase();
                MoveCategory moveCategory = getMoveCategory(categoryName);

                String stringPower = element.parent().parent().parent().child(2).child(1).text();
                int power = stringPower.equals("—") ? 0 : Integer.parseInt(stringPower);

                String stringAccuracy = element.parent().parent().parent().child(3).child(1).text();
                int accuracy = stringAccuracy.equals("∞") || stringAccuracy.equals("—") ? 100 : Integer.parseInt(stringAccuracy);

                String stringPP = element.parent().parent().parent().child(4).child(1).text();
                int PP = Integer.parseInt(stringPP.split(" ")[0]);

                String description;
                if (doc.getElementsByClass("sun").size() == 1) {
                    description = doc.getElementsByClass("sun").get(0).parent().parent().child(1).text();
                } else {
                    description = doc.getElementsByClass("sun").get(1).parent().parent().child(1).text();
                }

                moves.add(new Move(name, type, moveCategory, power, accuracy, PP, description));
            }
        }
        return moves;
    }

    private static MoveCategory getMoveCategory(String categoryName) {
        Bitmap categoryIcon = null;
        try {
            URL url = new URL(MOVE_CATEGORY_LINK.replace("?", categoryName));
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            categoryIcon = BitmapFactory.decodeStream(input);
        } catch (IOException e1) {
            e1.printStackTrace();
            System.exit(-1);
        }
        return new MoveCategory(categoryName, categoryIcon);
    }
}
