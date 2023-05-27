package app.connections.web_scrapping;

import static app.constants.StringConstants.MOVE_CATEGORY_LINK;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import app.data_objects.MoveCategory;

public class GetMoveCategory {
    public static MoveCategory get(String categoryName) {
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
