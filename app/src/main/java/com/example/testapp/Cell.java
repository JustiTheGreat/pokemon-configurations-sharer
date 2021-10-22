package com.example.testapp;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class Cell {
    private Bitmap image;
    private String species;
    private String name;
    private ArrayList<String> types;

    public Cell(Bitmap image, String species, String name, ArrayList<String> types) {
        this.image = image;
        this.species = species;
        this.name = name;
        this.types = types;
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
