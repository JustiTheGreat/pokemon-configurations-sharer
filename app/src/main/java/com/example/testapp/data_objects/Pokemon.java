package com.example.testapp.data_objects;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;

import com.example.testapp.PokemonConstants;

import java.util.ArrayList;

public class Pokemon implements PokemonConstants {
    private long ID;
    private String name;
    private String species;
    private String gender;
    private int level;
    private Ability ability;
    private Nature nature;
    private ArrayList<Integer> IVs;
    private ArrayList<Integer> EVs;
    private ArrayList<Move> moves;
    private ArrayList<Type> types;
    private Bitmap officialArt;
    private Bitmap sprite;
    private ArrayList<Integer> baseStats;

    public Pokemon(long ID, String name, String species, String gender, int level, Ability ability, Nature nature,
                   ArrayList<Integer> IVs, ArrayList<Integer> EVs, ArrayList<Move> moves,
                   ArrayList<Type> types, ArrayList<Integer> baseStats, Bitmap officialArt, Bitmap sprite) {
        this.ID = ID;
        this.name = name;
        this.species = species;
        this.gender = gender;
        this.level = level;
        this.ability = ability;
        this.nature = nature;
        this.IVs = IVs;
        this.EVs = EVs;
        this.moves = moves;
        this.types = types;
        this.baseStats = baseStats;
        this.officialArt = officialArt;
        this.sprite = sprite;
    }

    public long getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public String getSpecies() {
        return species;
    }

    public String getGender() {
        return gender;
    }

    public int getLevel() {
        return level;
    }

    public Ability getAbility() {
        return ability;
    }

    public Nature getNature() {
        return nature;
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

    public ArrayList<Type> getTypes() {
        return types;
    }

    public Bitmap getOfficialArt() {
        return officialArt;
    }

    public Bitmap getSprite() {
        return sprite;
    }

    public ArrayList<Integer> getBaseStats() {
        return baseStats;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setAbility(Ability ability) {
        this.ability = ability;
    }

    public void setNature(Nature nature) {
        this.nature = nature;
    }

    public void setIVs(ArrayList<Integer> IVs) {
        this.IVs = IVs;
    }

    public void setEVs(ArrayList<Integer> EVs) {
        this.EVs = EVs;
    }

    public void setMoves(ArrayList<Move> moves) {
        this.moves = moves;
    }

    public void setTypes(ArrayList<Type> types) {
        this.types = types;
    }

    public void setOfficialArt(Bitmap officialArt) {
        this.officialArt = officialArt;
    }

    public void setSprite(Bitmap sprite) {
        this.sprite = sprite;
    }

    public void setBaseStats(ArrayList<Integer> baseStats) {
        this.baseStats = baseStats;
    }

    @NonNull
    public String toStringOfTransmisibleData() {
        StringBuilder s = new StringBuilder(""
                + name + ";"
                + species + ";"
                + gender + ";"
                + level + ";"
                + ability.getName() + ";"
                + nature.getName()
        );
        for (Move move : moves) {
            s.append(";");
            if (move != null) s.append(move.getName());
        }
        for (int iv : IVs) {
             s.append(";").append(iv);
        }
        for (int ev : EVs) {
            s.append(";").append(ev);
        }
        s.append(";");
        return s.toString();
    }
}