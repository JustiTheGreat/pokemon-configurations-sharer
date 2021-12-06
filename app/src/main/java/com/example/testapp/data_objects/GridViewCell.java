package com.example.testapp.data_objects;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class GridViewCell {
    private final long id;
    private final Bitmap image;
    private final String name;
    private final String species;
    private final ArrayList<TYPE> types;

    public GridViewCell(long id, Bitmap image, String name, String species, ArrayList<TYPE> types) {
        this.id = id;
        this.image = image;
        this.name = name;
        this.species = species;
        this.types = types;
    }

    public long getId() {
        return id;
    }

    public Bitmap getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String getSpecies() {
        return species;
    }

    public ArrayList<TYPE> getTypes() {
        return types;
    }
}
