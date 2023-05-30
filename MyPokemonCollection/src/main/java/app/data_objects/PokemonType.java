package app.data_objects;

import com.mypokemoncollection.R;

import java.util.Arrays;
import java.util.List;

public class PokemonType {

    private static final PokemonType POKEMON_TYPE_NONE = new PokemonType("none", R.color.analogous_1);
    private static final PokemonType POKEMON_TYPE_NORMAL = new PokemonType("normal", R.color.normal);
    private static final PokemonType POKEMON_TYPE_FIGHTING = new PokemonType("fighting", R.color.fighting);
    private static final PokemonType POKEMON_TYPE_FLYING = new PokemonType("flying", R.color.flying);
    private static final PokemonType POKEMON_TYPE_POISON = new PokemonType("poison", R.color.poison);
    private static final PokemonType POKEMON_TYPE_GROUND = new PokemonType("ground", R.color.ground);
    private static final PokemonType POKEMON_TYPE_ROCK = new PokemonType("rock", R.color.rock);
    private static final PokemonType POKEMON_TYPE_BUG = new PokemonType("bug", R.color.bug);
    private static final PokemonType POKEMON_TYPE_GHOST = new PokemonType("ghost", R.color.ghost);
    private static final PokemonType POKEMON_TYPE_STEEL = new PokemonType("steel", R.color.steel);
    private static final PokemonType POKEMON_TYPE_FIRE = new PokemonType("fire", R.color.fire);
    private static final PokemonType POKEMON_TYPE_WATER = new PokemonType("water", R.color.water);
    private static final PokemonType POKEMON_TYPE_GRASS = new PokemonType("grass", R.color.grass);
    private static final PokemonType POKEMON_TYPE_ELECTRIC = new PokemonType("electric", R.color.electric);
    private static final PokemonType POKEMON_TYPE_PSYCHIC = new PokemonType("psychic", R.color.psychic);
    private static final PokemonType POKEMON_TYPE_ICE = new PokemonType("ice", R.color.ice);
    private static final PokemonType POKEMON_TYPE_DRAGON = new PokemonType("dragon", R.color.dragon);
    private static final PokemonType POKEMON_TYPE_DARK = new PokemonType("dark", R.color.dark);
    private static final PokemonType POKEMON_TYPE_FAIRY = new PokemonType("fairy", R.color.fairy);
    public static final List<PokemonType> POKEMON_TYPES = Arrays.asList(POKEMON_TYPE_NONE,
            POKEMON_TYPE_NORMAL, POKEMON_TYPE_FIGHTING, POKEMON_TYPE_FLYING, POKEMON_TYPE_POISON,
            POKEMON_TYPE_GROUND, POKEMON_TYPE_ROCK, POKEMON_TYPE_BUG, POKEMON_TYPE_GHOST,
            POKEMON_TYPE_STEEL, POKEMON_TYPE_FIRE, POKEMON_TYPE_WATER, POKEMON_TYPE_GRASS,
            POKEMON_TYPE_ELECTRIC, POKEMON_TYPE_PSYCHIC, POKEMON_TYPE_ICE, POKEMON_TYPE_DRAGON,
            POKEMON_TYPE_DARK, POKEMON_TYPE_FAIRY);

    private final String name;
    private final int color;

    private PokemonType(String name, int color) {
        this.name = name;
        this.color = color;
    }

    public static PokemonType getType(String typeName) {
        for (PokemonType POKEMON_TYPE : POKEMON_TYPES) {
            if (typeName.equalsIgnoreCase(POKEMON_TYPE.getName())) {
                return POKEMON_TYPE;
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
