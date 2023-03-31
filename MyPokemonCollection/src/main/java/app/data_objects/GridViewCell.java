package app.data_objects;

import android.graphics.Bitmap;

import java.util.List;

import lombok.Getter;

public class GridViewCell {
    @Getter private final String id;
    @Getter private final Bitmap image;
    @Getter private final String name;
    @Getter private final String species;
    @Getter private final List<Type> types;

    public GridViewCell(String id, Bitmap image, String name, String species, List<Type> types) {
        this.id = id;
        this.image = image;
        this.name = name;
        this.species = species;
        this.types = types;
    }
}
