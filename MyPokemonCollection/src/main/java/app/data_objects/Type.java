package app.data_objects;

import com.mypokemoncollection.R;

import java.util.Arrays;
import java.util.List;

public class Type {

    private static final Type TYPE_NORMAL = new Type("normal", R.color.normal);
    private static final Type TYPE_FIGHTING = new Type("fighting", R.color.fighting);
    private static final Type TYPE_FLYING = new Type("flying", R.color.flying);
    private static final Type TYPE_POISON = new Type("poison", R.color.poison);
    private static final Type TYPE_GROUND = new Type("ground", R.color.ground);
    private static final Type TYPE_ROCK = new Type("rock", R.color.rock);
    private static final Type TYPE_BUG = new Type("bug", R.color.bug);
    private static final Type TYPE_GHOST = new Type("ghost", R.color.ghost);
    private static final Type TYPE_STEEL = new Type("steel", R.color.steel);
    private static final Type TYPE_FIRE = new Type("fire", R.color.fire);
    private static final Type TYPE_WATER = new Type("water", R.color.water);
    private static final Type TYPE_GRASS = new Type("grass", R.color.grass);
    private static final Type TYPE_ELECTRIC = new Type("electric", R.color.electric);
    private static final Type TYPE_PSYCHIC = new Type("psychic", R.color.psychic);
    private static final Type TYPE_ICE = new Type("ice", R.color.ice);
    private static final Type TYPE_DRAGON = new Type("dragon", R.color.dragon);
    private static final Type TYPE_DARK = new Type("dark", R.color.dark);
    private static final Type TYPE_FAIRY = new Type("fairy", R.color.fairy);
    public static final List<Type> TYPES = Arrays.asList(TYPE_NORMAL, TYPE_FIGHTING, TYPE_FLYING, TYPE_POISON,
            TYPE_GROUND, TYPE_ROCK, TYPE_BUG, TYPE_GHOST, TYPE_STEEL, TYPE_FIRE, TYPE_WATER,
            TYPE_GRASS, TYPE_ELECTRIC, TYPE_PSYCHIC, TYPE_ICE, TYPE_DRAGON, TYPE_DARK, TYPE_FAIRY);

    private final String name;
    private final int color;

    private Type(String name, int color) {
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

    public String getName() {
        return name;
    }

    public int getColor() {
        return color;
    }
}
