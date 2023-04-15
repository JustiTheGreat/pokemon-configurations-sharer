package app.web_scrapping;

import static app.constants.StringConstants.POKEMON_PAGE_LINK;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class PokemonGenderIsKnown {
    public static boolean isKnown(long pokedexNumber) {
        Document doc = null;
        try {
            doc = Jsoup.connect(POKEMON_PAGE_LINK.replace("?", "" + pokedexNumber)).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert doc != null;

        Elements h2s = doc.select("h2");
        for (Element h2 : h2s) {
            if (h2.text().equals("Breeding")) {
                Elements ths = doc.select("th");
                for (Element th : ths) {
                    if (th.text().equals("Gender")) {
                        return th.nextElementSibling().children().size() == 2;
                    }
                }
            }
        }
        return false;
    }
}
