package com.example.testapp.data_objects;

import com.example.testapp.PokemonConstants;

import java.util.ArrayList;

public class PokemonConfiguration implements PokemonConstants {
    private final long id;
    private final String species;
    private final String name;
    private final String gender;
    private final String ability;
    private final String natureName;
    private final int level;
    private final ArrayList<Integer> IVs;
    private final ArrayList<Integer> EVs;
    private final ArrayList<Move> moves;

    public PokemonConfiguration(long id, String species, String name, String gender, String ability, String natureName, int level, ArrayList<Integer> IVs, ArrayList<Integer> EVs, ArrayList<Move> moves) {
        this.id = id;
        this.species = species;
        this.name = name;
        this.gender = gender;
        this.ability = ability;
        this.natureName = natureName;
        this.level = level;
        this.IVs = IVs;
        this.EVs = EVs;
        this.moves = moves;
    }

    public long getId() {
        return id;
    }

    public String getSpecies() {
        return species;
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public String getAbility() {
        return ability;
    }

    public String getNatureName() {
        return natureName;
    }

    public int getLevel() {
        return level;
    }

    public ArrayList<Integer> getIVs() {
        return IVs;
    }

    public ArrayList<Integer> getEVs() {
        return EVs;
    }

    public ArrayList<Move> getMoves() {
        return moves;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("" + species + ";" + name + ";" + gender + ";" + ability + ";" + natureName + ";" + level);
        for (int iv : IVs) s.append(":").append(iv);
        s.append(";");
        for (int ev : EVs) s.append(":").append(ev);
        return s.toString();
    }
}
