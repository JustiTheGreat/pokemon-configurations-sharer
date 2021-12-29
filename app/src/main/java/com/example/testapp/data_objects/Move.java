package com.example.testapp.data_objects;

public class Move {
    private final String name;
    private final Type type;
    private final MoveCategory category;
    private final int power;
    private final int accuracy;
    private final int PP;
    private final String description;

    public Move(String name, Type type, MoveCategory category, int power, int accuracy, int PP, String description) {
        this.name = name;
        this.type = type;
        this.category = category;
        this.power = power;
        this.accuracy = accuracy;
        this.PP = PP;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

    public MoveCategory getCategory() {
        return category;
    }

    public int getPower() {
        return power;
    }

    public int getAccuracy() {
        return accuracy;
    }

    public int getPP() {
        return PP;
    }

    public String getDescription() {
        return description;
    }

    public void print(){
        System.err.println(name+" "+type+" "+category+" "+power+" "+accuracy+" "+PP+" "+description);
    }
}
