package app.connections.web_scrapping;

import static app.constants.StringConstants.MOVES_LINK;
import static app.constants.StringConstants.MOVE_LINK;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

import app.data_objects.Move;
import app.data_objects.MoveCategory;
import app.data_objects.Type;

public class GetMove {
    public static Move get(String moveName) {
        if (moveName.isEmpty()) return null;
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
                MoveCategory moveCategory = GetMoveCategory.get(categoryName);

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
}
