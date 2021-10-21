package com.example.testapp;

import java.util.ArrayList;

public class PokemonConfiguration {
    private int id;
    private String species;
    private String name;
    private String gender;
    private Ability ability;
    private ArrayList<Move> moves;
    private int level;
    private ArrayList<Integer> IVs;
    private ArrayList<Integer> EVs;

    public PokemonConfiguration(int id, String species, String name, String gender, Ability ability, ArrayList<Move> moves, int level, ArrayList<Integer> IVs, ArrayList<Integer> EVs) {
        this.id = id;
        this.species = species;
        this.name = name;
        this.gender = gender;
        this.ability = ability;
        this.moves = moves;
        this.level = level;
        this.IVs = IVs;
        this.EVs = EVs;
    }

    public int getId() {
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

    public Ability getAbility() {
        return ability;
    }

    public ArrayList<Move> getMoves() {
        return moves;
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
}
