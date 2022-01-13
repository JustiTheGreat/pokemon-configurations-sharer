package app.constants;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.testapp.R;
import app.data_objects.Nature;
import app.data_objects.Type;

import java.util.ArrayList;

@RequiresApi(api = Build.VERSION_CODES.R)
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

    ArrayList<Type> TYPES = new ArrayList<Type>() {{
        add(TYPE_NORMAL);
        add(TYPE_FIGHTING);
        add(TYPE_FLYING);
        add(TYPE_POISON);
        add(TYPE_GROUND);
        add(TYPE_ROCK);
        add(TYPE_BUG);
        add(TYPE_GHOST);
        add(TYPE_STEEL);
        add(TYPE_FIRE);
        add(TYPE_WATER);
        add(TYPE_GRASS);
        add(TYPE_ELECTRIC);
        add(TYPE_PSYCHIC);
        add(TYPE_ICE);
        add(TYPE_DRAGON);
        add(TYPE_DARK);
        add(TYPE_FAIRY);
    }};

    static ArrayList<Double> createArrayListOfDouble(double[] list) {
        ArrayList<Double> arrayList = new ArrayList<>();
        for (double v : list) {
            arrayList.add(v);
        }
        return arrayList;
    }

    Nature NATURE_ADAMANT = new Nature("Adamant (+Atk, -SpA)", createArrayListOfDouble(new double[]{1.1, 1, 0.9, 1, 1}));
    Nature NATURE_BASHFUL = new Nature("Bashful", createArrayListOfDouble(new double[]{1, 1, 1, 1, 1}));
    Nature NATURE_BOLD = new Nature("Bold (+Def, -Atk)", createArrayListOfDouble(new double[]{0.9, 1.1, 1, 1, 1}));
    Nature NATURE_BRAVE = new Nature("Brave (+Atk, -Spe)", createArrayListOfDouble(new double[]{1.1, 1, 1, 1, 0.9}));
    Nature NATURE_CALM = new Nature("Calm (+SpD, -Atk)", createArrayListOfDouble(new double[]{0.9, 1, 1, 1.1, 1}));
    Nature NATURE_CAREFUL = new Nature("Careful (+SpD, -SpA)", createArrayListOfDouble(new double[]{1, 1, 0.9, 1.1, 1}));
    Nature NATURE_DOCILE = new Nature("Docile", createArrayListOfDouble(new double[]{1, 1, 1, 1, 1}));
    Nature NATURE_GENTLE = new Nature("Gentle (+SpD, -Def)", createArrayListOfDouble(new double[]{1, 0.9, 1, 1.1, 1}));
    Nature NATURE_HARDY = new Nature("Hardy", createArrayListOfDouble(new double[]{1, 1, 1, 1, 1}));
    Nature NATURE_HASTY = new Nature("Hasty (+Spe, -Def)", createArrayListOfDouble(new double[]{1, 0.9, 1, 1, 1.1}));
    Nature NATURE_IMPISH = new Nature("Impish (+Def, -SpA)", createArrayListOfDouble(new double[]{1, 1.1, 0.9, 1, 1}));
    Nature NATURE_JOLLY = new Nature("Jolly (+Spe, -SpA)", createArrayListOfDouble(new double[]{1, 1, 0.9, 1, 1.1}));
    Nature NATURE_LAX = new Nature("Lax (+Def, -SpD)", createArrayListOfDouble(new double[]{1, 1.1, 1, 0.9, 1}));
    Nature NATURE_LONELY = new Nature("Lonely (+Atk, -Def)", createArrayListOfDouble(new double[]{1.1, 0.9, 1, 1, 1}));
    Nature NATURE_MILD = new Nature("Mild (+SpA, -Def)", createArrayListOfDouble(new double[]{1, 0.9, 1.1, 1, 1}));
    Nature NATURE_MODEST = new Nature("Modest (+SpA, -Atk)", createArrayListOfDouble(new double[]{0.9, 1, 1.1, 1, 1}));
    Nature NATURE_NAIVE = new Nature("Naive (+Spe, -SpD)", createArrayListOfDouble(new double[]{1, 1, 1, 0.9, 1.1}));
    Nature NATURE_NAUGHTY = new Nature("Naughty (+Atk, -SpD)", createArrayListOfDouble(new double[]{1.1, 1, 1, 0.9, 1}));
    Nature NATURE_QUIET = new Nature("Quiet (+SpA, -Spe)", createArrayListOfDouble(new double[]{1, 1, 1.1, 1, 0.9}));
    Nature NATURE_QUIRKY = new Nature("Quirky", createArrayListOfDouble(new double[]{1, 1, 1, 1, 1}));
    Nature NATURE_RASH = new Nature("Rash (+SpA, -SpD)", createArrayListOfDouble(new double[]{1, 1, 1.1, 0.9, 1}));
    Nature NATURE_RELAXED = new Nature("Relaxed (+Def, -Spe)", createArrayListOfDouble(new double[]{1, 1.1, 1, 1, 0.9}));
    Nature NATURE_SASSY = new Nature("Sassy (+SpD, -Spe)", createArrayListOfDouble(new double[]{1, 1, 1, 1.1, 0.9}));
    Nature NATURE_SERIOUS = new Nature("Serious", createArrayListOfDouble(new double[]{1, 1, 1, 1, 1}));
    Nature NATURE_TIMID = new Nature("Timid (+Spe, -Atk)", createArrayListOfDouble(new double[]{0.9, 1, 1, 1, 1.1}));

    ArrayList<Nature> NATURES = new ArrayList<Nature>() {{
        add(NATURE_ADAMANT);
        add(NATURE_BASHFUL);
        add(NATURE_BOLD);
        add(NATURE_BRAVE);
        add(NATURE_CALM);
        add(NATURE_CAREFUL);
        add(NATURE_DOCILE);
        add(NATURE_GENTLE);
        add(NATURE_HARDY);
        add(NATURE_HASTY);
        add(NATURE_IMPISH);
        add(NATURE_JOLLY);
        add(NATURE_LAX);
        add(NATURE_LONELY);
        add(NATURE_MILD);
        add(NATURE_MODEST);
        add(NATURE_NAIVE);
        add(NATURE_NAUGHTY);
        add(NATURE_QUIET);
        add(NATURE_QUIRKY);
        add(NATURE_RASH);
        add(NATURE_RELAXED);
        add(NATURE_SASSY);
        add(NATURE_SERIOUS);
        add(NATURE_TIMID);
    }};

    @RequiresApi(api = Build.VERSION_CODES.N)
    static ArrayList<Integer> calculateStats(ArrayList<Integer> base, ArrayList<Integer> ivs,
                                             ArrayList<Integer> evs, int level, Nature nature) {
        ArrayList<Integer> total = new ArrayList<>(base);
        ArrayList<Double> natureMultipliers = nature.getEffects();
        for (int i = 0; i < total.size(); i++) {
            total.set(i, (int) (i == 0 ?
                    (2 * total.get(i)
                            + ivs.get(total.indexOf(total.get(i)))
                            + evs.get(total.indexOf(total.get(i))) / 4.0
                    ) * level / 100 + level + 10
                    :
                    ((2 * total.get(i)
                            + ivs.get(total.indexOf(total.get(i)))
                            + evs.get(total.indexOf(total.get(i))) / 4.0
                    ) * level / 100 + 5) * natureMultipliers.get(i - 1)
            ));
        }
        return total;
    }
}
