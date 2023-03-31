package app.constants;

import com.mypokemoncollection.R;

import java.util.Arrays;
import java.util.List;

import app.data_objects.Nature;
import app.data_objects.Type;

public interface PokemonConstants {
    int POKEDEX_NUMBER_LIMIT = 151;
    String MALE = "♂";
    String FEMALE = "♀";
    int NUMBER_OF_STATS = 6;

    Type TYPE_NORMAL = new Type("normal", R.color.normal);
    Type TYPE_FIGHTING = new Type("fighting", R.color.fighting);
    Type TYPE_FLYING = new Type("flying", R.color.flying);
    Type TYPE_POISON = new Type("poison", R.color.poison);
    Type TYPE_GROUND = new Type("ground", R.color.ground);
    Type TYPE_ROCK = new Type("rock", R.color.rock);
    Type TYPE_BUG = new Type("bug", R.color.bug);
    Type TYPE_GHOST = new Type("ghost", R.color.ghost);
    Type TYPE_STEEL = new Type("steel", R.color.steel);
    Type TYPE_FIRE = new Type("fire", R.color.fire);
    Type TYPE_WATER = new Type("water", R.color.water);
    Type TYPE_GRASS = new Type("grass", R.color.grass);
    Type TYPE_ELECTRIC = new Type("electric", R.color.electric);
    Type TYPE_PSYCHIC = new Type("psychic", R.color.psychic);
    Type TYPE_ICE = new Type("ice", R.color.ice);
    Type TYPE_DRAGON = new Type("dragon", R.color.dragon);
    Type TYPE_DARK = new Type("dark", R.color.dark);
    Type TYPE_FAIRY = new Type("fairy", R.color.fairy);

    List<Type> TYPES = Arrays.asList(TYPE_NORMAL, TYPE_FIGHTING, TYPE_FLYING, TYPE_POISON,
            TYPE_GROUND, TYPE_ROCK, TYPE_BUG, TYPE_GHOST, TYPE_STEEL, TYPE_FIRE, TYPE_WATER,
            TYPE_GRASS, TYPE_ELECTRIC, TYPE_PSYCHIC, TYPE_ICE, TYPE_DRAGON, TYPE_DARK, TYPE_FAIRY);

    double INCREASED = 1.1;
    double NORMAL = 1.0;
    double DECREASED = 0.9;
    Nature NATURE_ADAMANT = new Nature("Adamant (+Atk, -SpA)", Arrays.asList(INCREASED, NORMAL, DECREASED, NORMAL, NORMAL));
    Nature NATURE_BASHFUL = new Nature("Bashful", Arrays.asList(NORMAL, NORMAL, NORMAL, NORMAL, NORMAL));
    Nature NATURE_BOLD = new Nature("Bold (+Def, -Atk)", Arrays.asList(DECREASED, INCREASED, NORMAL, NORMAL, NORMAL));
    Nature NATURE_BRAVE = new Nature("Brave (+Atk, -Spe)", Arrays.asList(INCREASED, NORMAL, NORMAL, NORMAL, DECREASED));
    Nature NATURE_CALM = new Nature("Calm (+SpD, -Atk)", Arrays.asList(DECREASED, NORMAL, NORMAL, INCREASED, NORMAL));
    Nature NATURE_CAREFUL = new Nature("Careful (+SpD, -SpA)", Arrays.asList(NORMAL, NORMAL, DECREASED, INCREASED, NORMAL));
    Nature NATURE_DOCILE = new Nature("Docile", Arrays.asList(NORMAL, NORMAL, NORMAL, NORMAL, NORMAL));
    Nature NATURE_GENTLE = new Nature("Gentle (+SpD, -Def)", Arrays.asList(NORMAL, DECREASED, NORMAL, INCREASED, NORMAL));
    Nature NATURE_HARDY = new Nature("Hardy", Arrays.asList(NORMAL, NORMAL, NORMAL, NORMAL, NORMAL));
    Nature NATURE_HASTY = new Nature("Hasty (+Spe, -Def)", Arrays.asList(NORMAL, DECREASED, NORMAL, NORMAL, INCREASED));
    Nature NATURE_IMPISH = new Nature("Impish (+Def, -SpA)", Arrays.asList(NORMAL, INCREASED, DECREASED, NORMAL, NORMAL));
    Nature NATURE_JOLLY = new Nature("Jolly (+Spe, -SpA)", Arrays.asList(NORMAL, NORMAL, DECREASED, NORMAL, INCREASED));
    Nature NATURE_LAX = new Nature("Lax (+Def, -SpD)", Arrays.asList(NORMAL, INCREASED, NORMAL, DECREASED, NORMAL));
    Nature NATURE_LONELY = new Nature("Lonely (+Atk, -Def)", Arrays.asList(INCREASED, DECREASED, NORMAL, NORMAL, NORMAL));
    Nature NATURE_MILD = new Nature("Mild (+SpA, -Def)", Arrays.asList(NORMAL, DECREASED, INCREASED, NORMAL, NORMAL));
    Nature NATURE_MODEST = new Nature("Modest (+SpA, -Atk)", Arrays.asList(DECREASED, NORMAL, INCREASED, NORMAL, NORMAL));
    Nature NATURE_NAIVE = new Nature("Naive (+Spe, -SpD)", Arrays.asList(NORMAL, NORMAL, NORMAL, DECREASED, INCREASED));
    Nature NATURE_NAUGHTY = new Nature("Naughty (+Atk, -SpD)", Arrays.asList(INCREASED, NORMAL, NORMAL, DECREASED, NORMAL));
    Nature NATURE_QUIET = new Nature("Quiet (+SpA, -Spe)", Arrays.asList(NORMAL, NORMAL, INCREASED, NORMAL, DECREASED));
    Nature NATURE_QUIRKY = new Nature("Quirky", Arrays.asList(NORMAL, NORMAL, NORMAL, NORMAL, NORMAL));
    Nature NATURE_RASH = new Nature("Rash (+SpA, -SpD)", Arrays.asList(NORMAL, NORMAL, INCREASED, DECREASED, NORMAL));
    Nature NATURE_RELAXED = new Nature("Relaxed (+Def, -Spe)", Arrays.asList(NORMAL, INCREASED, NORMAL, NORMAL, DECREASED));
    Nature NATURE_SASSY = new Nature("Sassy (+SpD, -Spe)", Arrays.asList(NORMAL, NORMAL, NORMAL, INCREASED, DECREASED));
    Nature NATURE_SERIOUS = new Nature("Serious", Arrays.asList(NORMAL, NORMAL, NORMAL, NORMAL, NORMAL));
    Nature NATURE_TIMID = new Nature("Timid (+Spe, -Atk)", Arrays.asList(DECREASED, NORMAL, NORMAL, NORMAL, INCREASED));

    List<Nature> NATURES = Arrays.asList(NATURE_ADAMANT, NATURE_BASHFUL, NATURE_BOLD, NATURE_BRAVE,
            NATURE_CALM, NATURE_CAREFUL, NATURE_DOCILE, NATURE_GENTLE, NATURE_HARDY, NATURE_HASTY,
            NATURE_IMPISH, NATURE_JOLLY, NATURE_LAX, NATURE_LONELY, NATURE_MILD, NATURE_MODEST,
            NATURE_NAIVE, NATURE_NAUGHTY, NATURE_QUIET, NATURE_QUIRKY, NATURE_RASH, NATURE_RELAXED,
            NATURE_SASSY, NATURE_SERIOUS, NATURE_TIMID);
}
