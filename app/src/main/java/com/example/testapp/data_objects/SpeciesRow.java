package com.example.testapp.data_objects;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class SpeciesRow {
    private final Bitmap icon;
    private final String species;
    private final ArrayList<TYPE> types;

    public SpeciesRow(Bitmap icon, String species, ArrayList<TYPE> types) {
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

    public ArrayList<TYPE> getTypes() {
        return types;
    }
}

