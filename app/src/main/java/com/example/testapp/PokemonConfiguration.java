package com.example.testapp;

public class PokemonConfiguration {
    private int id;
    private String species;
    private String name;
    private String gender;
    private Ability ability;
    private Move[] moves;
    private int level;
    private int[] IVs;
    private int[] EVs;

    public PokemonConfiguration(int id, String species, String name, String gender, Ability ability, Move[] moves, int level, int[] IVs, int[] EVs) {
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

    public Move[] getMoves() {
        return moves;
    }

    public int getLevel() {
        return level;
    }

    public int[] getIVs() {
        return IVs;
    }

    public int[] getEVs() {
        return EVs;
    }
}
