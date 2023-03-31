package app.data_objects;

import lombok.Getter;

public class Move {
    @Getter private final String name;
    @Getter private final Type type;
    @Getter private final MoveCategory category;
    @Getter private final int power;
    @Getter private final int accuracy;
    @Getter private final int PP;
    @Getter private final String description;

    public Move(String name, Type type, MoveCategory category, int power, int accuracy, int PP, String description) {
        this.name = name;
        this.type = type;
        this.category = category;
        this.power = power;
        this.accuracy = accuracy;
        this.PP = PP;
        this.description = description;
    }
}
