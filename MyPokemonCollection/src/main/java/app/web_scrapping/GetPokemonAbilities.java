package app.web_scrapping;

import static app.constants.StringConstants.POKEMON_PAGE_LINK;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import app.data_objects.Ability;

public class GetPokemonAbilities {
    public static List<Ability> get(long pokedexNumber) {
        List<Ability> abilities = new ArrayList<>();
        Document doc = null;
        try {
            doc = Jsoup.connect(POKEMON_PAGE_LINK.replace("?", "" + pokedexNumber)).get();
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
}
