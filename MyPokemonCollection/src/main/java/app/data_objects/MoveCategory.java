package app.data_objects;

import android.graphics.Bitmap;

import lombok.Getter;

public class MoveCategory {
    @Getter private final String name;
    @Getter private final Bitmap icon;

    public MoveCategory(String name, Bitmap icon) {
        this.name = name;
        this.icon = icon;
    }
}
