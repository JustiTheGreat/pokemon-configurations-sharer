package app.data_objects;

public class Ability {

    private final String name;
    private final String description;

    public Ability(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Ability(String name) {
        this.name = name;
        this.description = null;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
