package com.example.testapp.data_objects;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class SpeciesRow {
    private Bitmap icon;
    private String species;
    private ArrayList<String> types;

    public SpeciesRow(Bitmap icon, String species, ArrayList<String> types) {
        this.icon = icon;
        this.species = species;
        this.types = types;
    }

    public Bitmap getIcon() {
        return icon;
    }

    public String getSpecies() {
        return species;
    }

    public ArrayList<String> getTypes() {
        return types;
    }
}

