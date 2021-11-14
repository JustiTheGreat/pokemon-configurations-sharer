package com.example.testapp.data_objects;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.testapp.PokemonConstants;

import java.util.ArrayList;

public class NATURE {
    private final String name;
    private final ArrayList<Double> effects;

    public NATURE(String name, ArrayList<Double> effects) {
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
    public static NATURE getNature(String natureName) {
        for (NATURE N : PokemonConstants.NATURES) {
            if (natureName.equals(N.getName())) {
                return N;
            }
        }
        return null;
    }
}
