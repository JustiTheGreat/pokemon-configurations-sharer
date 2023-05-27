package app.connections.web_scrapping;

import static app.constants.Gender.MALE_GENDER;
import static app.constants.Gender.UNKNOWN_GENDER;
import static app.constants.PokemonConstants.POKEDEX_NUMBER_LIMIT;
import static app.constants.StringConstants.ALL_POKEMON_LINK;

import android.os.Build;

import androidx.annotation.RequiresApi;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import app.data_objects.Pokemon;
import app.data_objects.Type;

public class GetAllPokemonSpeciesData {

    @RequiresApi(api = Build.VERSION_CODES.R)
    public static List<Pokemon> get() {
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
                String gender = PokemonGenderIsKnown.isKnown(pokedexNumber) ? MALE_GENDER : UNKNOWN_GENDER;
                pokemonList.add(Pokemon.newPokemon().pokedexNumber(pokedexNumber).species(species)
                                .gender(gender).types(types).baseStats(baseStats));
            }
        }
        return pokemonList;
    }
}
