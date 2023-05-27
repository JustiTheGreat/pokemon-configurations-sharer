package app.connections.web_scrapping;

import static app.constants.StringConstants.ALL_ABILITIES_LINK;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

import app.data_objects.Ability;

public class GetAbility {
    public static Ability get(String s) {
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
}
