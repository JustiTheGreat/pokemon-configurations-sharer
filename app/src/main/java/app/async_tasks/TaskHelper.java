package app.async_tasks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;

import androidx.annotation.RequiresApi;

import app.constants.PokemonConstants;
import app.constants.StringConstants;
import app.data_objects.Ability;
import app.data_objects.Move;
import app.data_objects.MoveCategory;
import app.data_objects.SpeciesRow;
import app.data_objects.Type;

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

public class TaskHelper implements PokemonConstants, StringConstants {

    @RequiresApi(api = Build.VERSION_CODES.R)
    public static ArrayList<SpeciesRow> getAllSpecies() {
        ArrayList<SpeciesRow> speciesRows = new ArrayList<>();
        Document doc = null;
        try {
            doc = Jsoup.connect(ALL_POKEMON_LINK).get();
        } catch (IOException e) {
            e.printStackTrace();
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
                Bitmap sprite = getPokemonSprite(e.text());
                String species = e.text();
                ArrayList<Type> types = getPokemonTypes(e);
                speciesRows.add(new SpeciesRow(sprite, species, types));
            }
        }
        return speciesRows;
    }

    public static Ability getPokemonAbility(String s) {
        Document doc = null;
        try {
            doc = Jsoup.connect(ALL_ABILITIES_LINK).get();
        } catch (IOException e) {
            e.printStackTrace();
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
    public static ArrayList<Type> getPokemonTypes(String s) {
        Document doc = null;
        try {
            doc = Jsoup.connect(ALL_POKEMON_LINK).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert doc != null;
        Elements elements = doc.getElementsByClass("ent-name");
        for (Element e : elements) {
            if (e.text().equals(s)) {
                return (ArrayList<Type>) e.parent().parent().getElementsByClass("type-icon")
                        .stream()
                        .map(Element::text)
                        .map(Type::getType)
                        .collect(Collectors.toList());
            }
        }
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static ArrayList<Type> getPokemonTypes(Element element) {
        return (ArrayList<Type>) element.parent().parent().getElementsByClass("type-icon")
                .stream()
                .map(Element::text)
                .map(Type::getType)
                .collect(Collectors.toList());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static ArrayList<Integer> getPokemonBaseStats(String s) {
        Document doc = null;
        try {
            doc = Jsoup.connect(ALL_POKEMON_LINK).get();
        } catch (IOException e) {
            e.printStackTrace();
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
                }

                Element element = doc.select("a[href^=/type/]").get(1);

                Type type = Type.getType(element.text());

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
                }

                String name = doc.select("h1").text().replace(" (move)", "");

                Element element = doc.select("a[href^=/type/]").get(1);

                Type type = Type.getType(element.text());

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
                } else if (doc.getElementsByClass("sun").size() == 2){
                    description = doc.getElementsByClass("sun").get(1).parent().parent().child(1).text();
                } else description = "problem";

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
        }
        return new MoveCategory(categoryName, categoryIcon);
    }
}
