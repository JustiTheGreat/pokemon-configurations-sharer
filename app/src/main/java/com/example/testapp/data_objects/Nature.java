package com.example.testapp.data_objects;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.testapp.constants.PokemonConstants;

import java.util.ArrayList;

public class Nature implements PokemonConstants{
    private final String name;
    private final ArrayList<Double> effects;

    public Nature(String name, ArrayList<Double> effects) {
        this.name = name;
        this.effects = effects;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Double> getEffects() {
        return effects;
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    public static Nature getNature(String natureName) {
        for (Nature N : NATURES) {
            if (natureName.equals(N.getName())) {
                return N;
            }
        }
        return null;
    }
}
