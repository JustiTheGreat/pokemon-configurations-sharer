package app.data_objects;

import android.os.Build;

import androidx.annotation.RequiresApi;

import app.constants.PokemonConstants;

public class Type implements PokemonConstants{
    private final String name;
    private final int color;

    public Type(String name, int color) {
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public int getColor() {
        return color;
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    public static Type getType(String typeName) {
        for (Type TYPE : TYPES) {
            if (typeName.equalsIgnoreCase(TYPE.getName())) {
                return TYPE;
            }
        }
        return null;
    }
}
