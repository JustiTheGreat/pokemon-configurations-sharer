package app.data_objects;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class SpeciesRow {
    private final Bitmap icon;
    private final String species;
    private final ArrayList<Type> types;

    public SpeciesRow(Bitmap icon, String species, ArrayList<Type> types) {
        this.icon = icon;
        this.species = species;
        this.types = types;
    }

    public Bitmap getSprite() {
        return icon;
    }

    public String getSpecies() {
        return species;
    }

    public ArrayList<Type> getTypes() {
        return types;
    }
}

