package com.example.testapp.data_objects;

import com.example.testapp.PokemonConstants;

import java.util.ArrayList;

public class PokemonConfiguration implements PokemonConstants {
    private long id;
    private String species;
    private String name;
    private String gender;
    private String ability;
    private String nature;
    private int level;
    private ArrayList<Integer> IVs;
    private ArrayList<Integer> EVs;
    private ArrayList<Move> moves;

    public PokemonConfiguration(long id, String species, String name, String gender, String ability, String nature, int level, ArrayList<Integer> IVs, ArrayList<Integer> EVs, ArrayList<Move> moves) {
        this.id = id;
        this.species = species;
        this.name = name;
        this.gender = gender;
        this.ability = ability;
        this.nature = nature;
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

    public String getNature() {
        return nature;
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
}
