package app.data_objects;

import app.constants.PokemonConstants;
import lombok.Getter;

public class Type implements PokemonConstants{
    @Getter private final String name;
    @Getter private final int color;

    public Type(String name, int color) {
        this.name = name;
        this.color = color;
    }

    public static Type getType(String typeName) {
        for (Type TYPE : TYPES) {
            if (typeName.equalsIgnoreCase(TYPE.getName())) {
                return TYPE;
            }
        }
        return null;
    }
}
