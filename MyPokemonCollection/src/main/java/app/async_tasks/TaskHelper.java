package app.async_tasks;

import static app.constants.Gender.MALE_GENDER;
import static app.constants.Gender.UNKNOWN_GENDER;
import static app.constants.PokemonConstants.POKEDEX_NUMBER_LIMIT;
import static app.constants.StringConstants.ALL_ABILITIES_LINK;
import static app.constants.StringConstants.ALL_POKEMON_LINK;
import static app.constants.StringConstants.MOVES_LINK;
import static app.constants.StringConstants.MOVE_CATEGORY_LINK;
import static app.constants.StringConstants.MOVE_LINK;
import static app.constants.StringConstants.POKEMON_MOVES_LINK;
import static app.constants.StringConstants.POKEMON_PAGE_LINK;
import static app.constants.StringConstants.POKEMON_SPRITE_LINK;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;

import androidx.annotation.RequiresApi;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import app.data_objects.Ability;
import app.data_objects.Move;
import app.data_objects.MoveCategory;
import app.data_objects.Pokemon;
import app.data_objects.Type;

public class TaskHelper {

    @RequiresApi(api = Build.VERSION_CODES.R)
    public static void getPokemonData(Pokemon pokemon) {
        Document doc = null;
        try {
            doc = Jsoup.connect(ALL_POKEMON_LINK).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert doc != null;

        Elements elements = doc.getElementsByClass("infocard-cell-data");
        for (Element e : elements) {
            long value = Long.parseLong(e.text().replaceFirst("^0+(?!$)", ""));
            if (value == pokemon.getPokedexNumber()) {
                Element parent = e.parent().parent();
                String species = parent.getElementsByClass("ent-name").text();
                List<Type> types = parent.getElementsByClass("type-icon").stream()
                        .map(Element::text)
                        .map(Type::getType)
                        .collect(Collectors.toList());
                List<Long> baseStats = parent.getElementsByClass("cell-num").stream()
                        .map(bs -> Long.parseLong(bs.text()))
                        .collect(Collectors.toList());
                baseStats.remove(0);
                baseStats.remove(0);
                pokemon.setSpecies(species);
                pokemon.setTypes(types);
                pokemon.setBaseStats(baseStats);
                break;
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    public static List<Pokemon> getAllPokemonData() {
        List<Pokemon> pokemonList = new ArrayList<>();
        Document doc = null;
        try {
            doc = Jsoup.connect(ALL_POKEMON_LINK).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert doc != null;
        Elements elements = doc.getElementsByClass("infocard-cell-data");
        boolean already_exists;
        for (Element e : elements) {
            long pokedexNumber = Long.parseLong(e.text().replaceFirst("^0+(?!$)", ""));
            if (pokedexNumber > POKEDEX_NUMBER_LIMIT) break;
            already_exists = false;
            for (Pokemon pokemon : pokemonList) {
                if (pokemon.getPokedexNumber() == pokedexNumber) {
                    already_exists = true;
                    break;
                }
            }

            if (!already_exists) {
                Bitmap sprite = getPokemonSprite(pokedexNumber);
                Element parent = e.parent().parent();
                String species = parent.getElementsByClass("ent-name").text();
                List<Type> types = parent.getElementsByClass("type-icon").stream()
                        .map(Element::text)
                        .map(Type::getType)
                        .collect(Collectors.toList());
                List<Long> baseStats = parent.getElementsByClass("cell-num").stream()
                        .map(bs -> Long.parseLong(bs.text()))
                        .collect(Collectors.toList());
                baseStats.remove(0);
                baseStats.remove(0);
                String gender = isPokemonGenderKnown(pokedexNumber) ? MALE_GENDER : UNKNOWN_GENDER;
                pokemonList.add(new Pokemon(pokedexNumber, species, gender, types, baseStats, sprite));
            }
        }
        return pokemonList;
    }

    private static boolean isPokemonGenderKnown(long pokedexNumber) {
        Document doc = null;
        try {
            doc = Jsoup.connect(POKEMON_PAGE_LINK.replace("?", "" + pokedexNumber)).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert doc != null;

        Elements h2s = doc.select("h2");
        for(Element h2 : h2s){
            if(h2.text().equals("Breeding")){
                Elements ths = doc.select("th");
                for(Element th : ths) {
                    if(th.text().equals("Gender")){
                        System.out.println(th.nextElementSibling().children().size() == 2);
                        return th.nextElementSibling().children().size() == 2;
                    }
                }
            }
        }
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    public static Bitmap getPokemonSprite(long pokedexNumber) {
        try {
            URL url = new URL(POKEMON_SPRITE_LINK.replace("?", String.format("%03d", pokedexNumber)));
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            connection.disconnect();
            return bitmap;
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
        return null;
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

//    @RequiresApi(api = Build.VERSION_CODES.N)
//    public static List<Type> getPokemonTypes(long species) {
//        Document doc = null;
//        try {
//            doc = Jsoup.connect(ALL_POKEMON_LINK).get();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        assert doc != null;
////        Elements elements = doc.getElementsByClass("ent-name");
//        Elements elements = doc.getElementsByClass("infocard-cell-data");
//        for (Element e : elements) {
//            long value = Long.parseLong(e.text().replaceFirst("^0+(?!$)", ""));
//            if (value == species) {
//                return e.parent().parent().getElementsByClass("type-icon")
//                        .stream()
//                        .map(Element::text)
//                        .map(Type::getType)
//                        .collect(Collectors.toList());
//            }
//        }
//        return null;
//    }

//    @RequiresApi(api = Build.VERSION_CODES.N)
//    public static ArrayList<Type> getPokemonTypes(Element element) {
//        return (ArrayList<Type>) element.parent().parent().getElementsByClass("type-icon")
//                .stream()
//                .map(Element::text)
//                .map(Type::getType)
//                .collect(Collectors.toList());
//    }

//    @RequiresApi(api = Build.VERSION_CODES.N)
//    public static List<Integer> getPokemonBaseStats(long species) {
//        Document doc = null;
//        try {
//            doc = Jsoup.connect(ALL_POKEMON_LINK).get();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        assert doc != null;
//        Elements elements = doc.getElementsByClass("ent-name");
//        for (Element e : elements) {
//            if (e.text().equals(s)) {
//                ArrayList<Integer> baseStats = (ArrayList<Integer>) e.parent().parent().getElementsByClass("cell-num")
//                        .stream().map(bs -> Integer.parseInt(bs.text())).collect(Collectors.toList());
//                baseStats.remove(0);
//                return baseStats;
//            }
//        }
//        return null;
//    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    public static Move getMove(String moveName) {
        if(moveName.isEmpty())return null;
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
    public static List<Ability> getPokemonAbilities(long pokedexNumber) {
        List<Ability> abilities = new ArrayList<>();
        Document doc = null;
        try {
            doc = Jsoup.connect(POKEMON_PAGE_LINK.replace("?", ""+pokedexNumber)).get();
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
        assert doc != null;
        Elements elements = doc.select("a[href^=/ability/]");
        for (Element e : elements) {
            abilities.add(new Ability(e.text(), e.attr("title")));
        }
        return abilities;
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    public static ArrayList<Move> getPokemonMoves(long pokedexNumber) {
        Document doc = null;
        try {
            doc = Jsoup.connect(POKEMON_MOVES_LINK.replace("?", ""+pokedexNumber)).get();
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
            connection.disconnect();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return new MoveCategory(categoryName, categoryIcon);
    }
}
