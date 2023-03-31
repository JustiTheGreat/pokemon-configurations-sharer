package app.data_objects;

import android.graphics.Bitmap;

import java.util.ArrayList;

import lombok.Getter;

public class SpeciesRow {
    @Getter private final Bitmap icon;
    @Getter private final String species;
    @Getter private final ArrayList<Type> types;

    public SpeciesRow(Bitmap icon, String species, ArrayList<Type> types) {
        this.icon = icon;
        this.species = species;
        this.types = types;
    }
}

