package com.example.testapp;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.testapp.async_tasks.Helper;
import com.example.testapp.data_objects.Nature;
import com.example.testapp.data_objects.TYPE;

import java.util.ArrayList;

@RequiresApi(api = Build.VERSION_CODES.R)
public interface PokemonConstants {
    int POKEDEX_NUMBER_LIMIT = 10;
    String MALE = "♂";
    String FEMALE = "♀";
    String PHYSICAL = "Physical";
    String SPECIAL = "Special";
    String STATUS = "Status";
    int NUMBER_OF_STATS = 6;

    TYPE TYPE_NORMAL = new TYPE("normal", R.color.normal);
    TYPE TYPE_FIGHTING = new TYPE("fighting", R.color.fighting);
    TYPE TYPE_FLYING = new TYPE("flying", R.color.flying);
    TYPE TYPE_POISON = new TYPE("poison", R.color.poison);
    TYPE TYPE_GROUND = new TYPE("ground", R.color.ground);
    TYPE TYPE_ROCK = new TYPE("rock", R.color.rock);
    TYPE TYPE_BUG = new TYPE("bug", R.color.bug);
    TYPE TYPE_GHOST = new TYPE("ghost", R.color.ghost);
    TYPE TYPE_STEEL = new TYPE("steel", R.color.steel);
    TYPE TYPE_FIRE = new TYPE("fire", R.color.fire);
    TYPE TYPE_WATER = new TYPE("water", R.color.water);
    TYPE TYPE_GRASS = new TYPE("grass", R.color.grass);
    TYPE TYPE_ELECTRIC = new TYPE("electric", R.color.electric);
    TYPE TYPE_PSYCHIC = new TYPE("psychic", R.color.psychic);
    TYPE TYPE_ICE = new TYPE("ice", R.color.ice);
    TYPE TYPE_DRAGON = new TYPE("dragon", R.color.dragon);
    TYPE TYPE_DARK = new TYPE("dark", R.color.dark);
    TYPE TYPE_FAIRY = new TYPE("fairy", R.color.fairy);

    ArrayList<TYPE> TYPES = new ArrayList<TYPE>() {{
        add(TYPE_NORMAL);
        add(TYPE_FIGHTING);
        add(TYPE_FLYING);
        add(TYPE_POISON);
        add(TYPE_GROUND);
        add(TYPE_ROCK);
        add(TYPE_BUG);
        add(TYPE_GHOST);
        add(TYPE_STEEL);
        add(TYPE_FIRE);
        add(TYPE_WATER);
        add(TYPE_GRASS);
        add(TYPE_ELECTRIC);
        add(TYPE_PSYCHIC);
        add(TYPE_ICE);
        add(TYPE_DRAGON);
        add(TYPE_DARK);
        add(TYPE_FAIRY);
    }};

    Nature NATURE_ADAMANT = new Nature("Adamant (+Atk, -SpA)", Helper.createArrayListOfDouble(new double[]{1.1, 1, 0.9, 1, 1}));
    Nature NATURE_BASHFUL = new Nature("Bashful", Helper.createArrayListOfDouble(new double[]{1, 1, 1, 1, 1}));
    Nature NATURE_BOLD = new Nature("Bold (+Def, -Atk)", Helper.createArrayListOfDouble(new double[]{0.9, 1.1, 1, 1, 1}));
    Nature NATURE_BRAVE = new Nature("Brave (+Atk, -Spe)", Helper.createArrayListOfDouble(new double[]{1.1, 1, 1, 1, 0.9}));
    Nature NATURE_CALM = new Nature("Calm (+SpD, -Atk)", Helper.createArrayListOfDouble(new double[]{0.9, 1, 1, 1.1, 1}));
    Nature NATURE_CAREFUL = new Nature("Careful (+SpD, -SpA)", Helper.createArrayListOfDouble(new double[]{1, 1, 0.9, 1.1, 1}));
    Nature NATURE_DOCILE = new Nature("Docile", Helper.createArrayListOfDouble(new double[]{1, 1, 1, 1, 1}));
    Nature NATURE_GENTLE = new Nature("Gentle (+SpD, -Def)", Helper.createArrayListOfDouble(new double[]{1, 0.9, 1, 1.1, 1}));
    Nature NATURE_HARDY = new Nature("Hardy", Helper.createArrayListOfDouble(new double[]{1, 1, 1, 1, 1}));
    Nature NATURE_HASTY = new Nature("Hasty (+Spe, -Def)", Helper.createArrayListOfDouble(new double[]{1, 0.9, 1, 1, 1.1}));
    Nature NATURE_IMPISH = new Nature("Impish (+Def, -SpA)", Helper.createArrayListOfDouble(new double[]{1, 1.1, 0.9, 1, 1}));
    Nature NATURE_JOLLY = new Nature("Jolly (+Spe, -SpA)", Helper.createArrayListOfDouble(new double[]{1, 1, 0.9, 1, 1.1}));
    Nature NATURE_LAX = new Nature("Lax (+Def, -SpD)", Helper.createArrayListOfDouble(new double[]{1, 1.1, 1, 0.9, 1}));
    Nature NATURE_LONELY = new Nature("Lonely (+Atk, -Def)", Helper.createArrayListOfDouble(new double[]{1.1, 0.9, 1, 1, 1}));
    Nature NATURE_MILD = new Nature("Mild (+SpA, -Def)", Helper.createArrayListOfDouble(new double[]{1, 0.9, 1.1, 1, 1}));
    Nature NATURE_MODEST = new Nature("Modest (+SpA, -Atk)", Helper.createArrayListOfDouble(new double[]{0.9, 1, 1.1, 1, 1}));
    Nature NATURE_NAIVE = new Nature("Naive (+Spe, -SpD)", Helper.createArrayListOfDouble(new double[]{1, 1, 1, 0.9, 1.1}));
    Nature NATURE_NAUGHTY = new Nature("Naughty (+Atk, -SpD)", Helper.createArrayListOfDouble(new double[]{1.1, 1, 1, 0.9, 1}));
    Nature NATURE_QUIET = new Nature("Quiet (+SpA, -Spe)", Helper.createArrayListOfDouble(new double[]{1, 1, 1.1, 1, 0.9}));
    Nature NATURE_QUIRKY = new Nature("Quirky", Helper.createArrayListOfDouble(new double[]{1, 1, 1, 1, 1}));
    Nature NATURE_RASH = new Nature("Rash (+SpA, -SpD)", Helper.createArrayListOfDouble(new double[]{1, 1, 1.1, 0.9, 1}));
    Nature NATURE_RELAXED = new Nature("Relaxed (+Def, -Spe)", Helper.createArrayListOfDouble(new double[]{1, 1.1, 1, 1, 0.9}));
    Nature NATURE_SASSY = new Nature("Sassy (+SpD, -Spe)", Helper.createArrayListOfDouble(new double[]{1, 1, 1, 1.1, 0.9}));
    Nature NATURE_SERIOUS = new Nature("Serious", Helper.createArrayListOfDouble(new double[]{1, 1, 1, 1, 1}));
    Nature NATURE_TIMID = new Nature("Timid (+Spe, -Atk)", Helper.createArrayListOfDouble(new double[]{0.9, 1, 1, 1, 1.1}));

    ArrayList<Nature> NATURES = new ArrayList<Nature>() {{
        add(NATURE_ADAMANT);
        add(NATURE_BASHFUL);
        add(NATURE_BOLD);
        add(NATURE_BRAVE);
        add(NATURE_CALM);
        add(NATURE_CAREFUL);
        add(NATURE_DOCILE);
        add(NATURE_GENTLE);
        add(NATURE_HARDY);
        add(NATURE_HASTY);
        add(NATURE_IMPISH);
        add(NATURE_JOLLY);
        add(NATURE_LAX);
        add(NATURE_LONELY);
        add(NATURE_MILD);
        add(NATURE_MODEST);
        add(NATURE_NAIVE);
        add(NATURE_NAUGHTY);
        add(NATURE_QUIET);
        add(NATURE_QUIRKY);
        add(NATURE_RASH);
        add(NATURE_RELAXED);
        add(NATURE_SASSY);
        add(NATURE_SERIOUS);
        add(NATURE_TIMID);
    }};
}
