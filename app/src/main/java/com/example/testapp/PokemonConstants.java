package com.example.testapp;

import java.util.ArrayList;

public interface PokemonConstants {
    String MALE = "male";
    String FEM = "female";
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
    //    String[] TYPE_NORMAL = {"normal", "#A8A77A"};
//    String[] TYPE_FIGHTING = {"fighting", "#C22E28"};
//    String[] TYPE_FLYING = {"flying", "#A98FF3"};
//    String[] TYPE_POISON = {"poison", "#A33EA1"};
//    String[] TYPE_GROUND = {"ground", "#E2BF65"};
//    String[] TYPE_ROCK = {"rock", "#B6A136"};
//    String[] TYPE_BUG = {"bug", "#A6B91A"};
//    String[] TYPE_GHOST = {"ghost", "#735797"};
//    String[] TYPE_STEEL = {"steel", "#B7B7CE"};
//    String[] TYPE_FIRE = {"fire", "#EE8130"};
//    String[] TYPE_WATER = {"water", "#6390F0"};
//    String[] TYPE_GRASS = {"grass", "#7AC74C"};
//    String[] TYPE_ELECTRIC = {"electric", "#F7D02C"};
//    String[] TYPE_PSYCHIC = {"psychic", "#F95587"};
//    String[] TYPE_ICE = {"ice", "#96D9D6"};
//    String[] TYPE_DRAGON = {"dragon", "#6F35FC"};
//    String[] TYPE_DARK = {"dark", "#705746"};
//    String[] TYPE_FAIRY = {"fairy", "#D685AD"};
    ArrayList<TYPE> TYPES = new ArrayList<TYPE>() {
        {
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
        }
    };

    class TYPE {
        private String name;
        private int color;

        public TYPE(String name, int color) {
            this.name = name;
            this.color = color;
        }

        public String getName() {
            return name;
        }

        public int getColor() {
            return color;
        }
    }
}
