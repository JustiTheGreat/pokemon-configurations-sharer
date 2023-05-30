package app.data_objects;

public class Move {

    private final String name;
    private final PokemonType pokemonType;
    private final MoveCategory category;
    private final int power;
    private final int accuracy;
    private final int pp;
    private final String description;

    public Move(String name, PokemonType pokemonType, MoveCategory category, int power, int accuracy, int pp, String description) {
        this.name = name;
        this.pokemonType = pokemonType;
        this.category = category;
        this.power = power;
        this.accuracy = accuracy;
        this.pp = pp;
        this.description = description;
    }

    public Move(String name) {
        this.name = name;
        pokemonType = null;
        category = null;
        power = -1;
        accuracy = -1;
        pp = -1;
        description = null;
    }

    public String getName() {
        return name;
    }

    public PokemonType getType() {
        return pokemonType;
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

    public int getPp() {
        return pp;
    }

    public String getDescription() {
        return description;
    }
}
