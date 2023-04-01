package app.data_objects;

import lombok.Getter;

public class Ability {
    @Getter
    private final String name;
    @Getter
    private final String description;

    public Ability(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Ability(String name) {
        this.name = name;
        description = null;
    }
}
