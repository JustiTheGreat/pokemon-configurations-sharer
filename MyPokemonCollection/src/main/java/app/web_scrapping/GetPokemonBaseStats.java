package app.web_scrapping;

import static app.constants.StringConstants.ALL_POKEMON_LINK;

import android.os.Build;

import androidx.annotation.RequiresApi;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class GetPokemonBaseStats {
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static List<Long> get(long pokedexNumber) {
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
            if (value == pokedexNumber) {
                Element parent = e.parent().parent();
                List<Long> baseStats = parent.getElementsByClass("cell-num").stream()
                        .map(bs -> Long.parseLong(bs.text()))
                        .collect(Collectors.toList());
                baseStats.remove(0);
                baseStats.remove(0);
                return baseStats;
            }
        }
        return null;
    }
}
