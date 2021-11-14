package com.example.testapp.data_objects;

import java.util.ArrayList;

public class StatsRow {
    private final int level;
    private final String nature;
    private final ArrayList<Integer> IVs;
    private final ArrayList<Integer> EVs;

    public StatsRow(int level, String nature, ArrayList<Integer> IVs, ArrayList<Integer> EVs) {
        this.level = level;
        this.nature = nature;
        this.IVs = IVs;
        this.EVs = EVs;
    }

    public int getLevel() {
        return level;
    }

    public String getNature() {
        return nature;
    }

    public ArrayList<Integer> getIVs() {
        return IVs;
    }

    public ArrayList<Integer> getEVs() {
        return EVs;
    }
}
