package com.example.testapp.data_objects;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class GridViewCell {
    private long id;
    private Bitmap image;
    private String species;
    private String name;
    private ArrayList<String> types;

    public GridViewCell(long id, Bitmap image, String species, String name, ArrayList<String> types) {
        this.id = id;
        this.image = image;
        this.species = species;
        this.name = name;
        this.types = types;
    }

    public long getId() {
        return id;
    }

    public Bitmap getImage() {
        return image;
    }

    public String getSpecies() {
        return species;
    }

    public String getName() {
        return name;
    }

    public ArrayList<String> getTypes() {
        return types;
    }
}
