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

    NATURE NATURE_ADAMANT = new NATURE("Adamant (+Atk, -SpA)", 1.1, 1, 0.9, 1, 1);
    NATURE NATURE_BASHFUL = new NATURE("Bashful", 1, 1, 1, 1, 1);
    NATURE NATURE_BOLD = new NATURE("Bold (+Def, -Atk)", 0.9, 1.1, 1, 1, 1);
    NATURE NATURE_BRAVE = new NATURE("Brave (+Atk, -Spe)", 1.1, 1, 1, 1, 0.9);
    NATURE NATURE_CALM = new NATURE("Calm (+SpD, -Atk)", 0.9, 1, 1, 1.1, 1);
    NATURE NATURE_CAREFUL = new NATURE("Careful (+SpD, -SpA)", 1, 1, 0.9, 1.1, 1);
    NATURE NATURE_DOCILE = new NATURE("Docile", 1, 1, 1, 1, 1);
    NATURE NATURE_GENTLE = new NATURE("Gentle (+SpD, -Def)", 1, 0.9, 1, 1.1, 1);
    NATURE NATURE_HARDY = new NATURE("Hardy", 1, 1, 1, 1, 1);
    NATURE NATURE_HASTY = new NATURE("Hasty (+Spe, -Def)", 1, 0.9, 1, 1, 1.1);
    NATURE NATURE_IMPISH = new NATURE("Impish (+Def, -SpA)", 1, 1.1, 0.9, 1, 1);
    NATURE NATURE_JOLLY = new NATURE("Jolly (+Spe, -SpA)", 1, 1, 0.9, 1, 1.1);
    NATURE NATURE_LAX = new NATURE("Lax (+Def, -SpD)", 1, 1.1, 1, 0.9, 1);
    NATURE NATURE_LONELY = new NATURE("Lonely (+Atk, -Def)", 1.1, 0.9, 1, 1, 1);
    NATURE NATURE_MILD = new NATURE("Mild (+SpA, -Def)", 1, 0.9, 1.1, 1, 1);
    NATURE NATURE_MODEST = new NATURE("Modest (+SpA, -Atk)", 0.9, 1, 1.1, 1, 1);
    NATURE NATURE_NAIVE = new NATURE("Naive (+Spe, -SpD)", 1, 1, 1, 0.9, 1.1);
    NATURE NATURE_NAUGHTY = new NATURE("Naughty (+Atk, -SpD)", 1.1, 1, 1, 0.9, 1);
    NATURE NATURE_QUIET = new NATURE("Quiet (+SpA, -Spe)", 1, 1, 1.1, 1, 0.9);
    NATURE NATURE_QUIRKY = new NATURE("Quirky", 1, 1, 1, 1, 1);
    NATURE NATURE_RASH = new NATURE("Rash (+SpA, -SpD)", 1, 1, 1.1, 0.9, 1);
    NATURE NATURE_RELAXED = new NATURE("Relaxed (+Def, -Spe)", 1, 1.1, 1, 1, 0.9);
    NATURE NATURE_SASSY = new NATURE("Sassy (+SpD, -Spe)", 1, 1, 1, 1.1, 0.9);
    NATURE NATURE_SERIOUS = new NATURE("Serious", 1, 1, 1, 1, 1);
    NATURE NATURE_TIMID = new NATURE("Timid (+Spe, -Atk)", 0.9, 1, 1, 1, 1.1);
    ArrayList<NATURE> NATURES = new ArrayList<NATURE>() {{
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
    //            switch(name){
//                case "Adamant":
//                    break;
//                case "Bold":
//                    break;
//                case "Brave":
//                    break;
//                case "Calm":
//                    break;
//                case "Careful":
//                    break;
//                case "Gentle":
//                    break;
//                case "Hasty":
//                    break;
//                case "Impish":
//                    break;
//                case "Jolly":
//                    break;
//                case "Lax":
//                    break;
//                case "Lonely":
//                    break;
//                case "Mild":
//                    break;
//                case "Modest":
//                    break;
//                case "Naive":
//                    break;
//                case "Naughty":
//                    break;
//                case "Quiet":
//                    break;
//                case "Rash":
//                    break;
//                case "Relaxed":
//                    break;
//                case "Sassy":
//                    break;
//                case "Timid":
//                    break;
//                case "Docile","Hardy","Quirky","Serious":
//                    break;
//                case "Bashful":
//                    break;
//
//            }

    class NATURE {
        private String name;
        private double attack;
        private double defense;
        private double specialAttack;
        private double specialDefense;
        private double speed;

        public NATURE(String name, double attack, double defense, double specialAttack, double specialDefense, double speed) {
            this.name = name;
            this.attack = attack;
            this.defense = defense;
            this.specialAttack = specialAttack;
            this.specialDefense = specialDefense;
            this.speed = speed;
        }

        public String getName() {
            return name;
        }

        public double getAttack() {
            return attack;
        }

        public double getDefense() {
            return defense;
        }

        public double getSpecialAttack() {
            return specialAttack;
        }

        public double getSpecialDefense() {
            return specialDefense;
        }

        public double getSpeed() {
            return speed;
        }
    }
}
