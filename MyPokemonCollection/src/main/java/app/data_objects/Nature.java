package app.data_objects;

import java.util.Arrays;
import java.util.List;

import lombok.Getter;

public class Nature {
    private static final double INCREASED = 1.1;
    private static final double NORMAL = 1.0;
    private static final double DECREASED = 0.9;
    private static final Nature NATURE_ADAMANT = new Nature("Adamant (+Atk, -SpA)", Arrays.asList(INCREASED, NORMAL, DECREASED, NORMAL, NORMAL));
    private static final Nature NATURE_BASHFUL = new Nature("Bashful", Arrays.asList(NORMAL, NORMAL, NORMAL, NORMAL, NORMAL));
    private static final Nature NATURE_BOLD = new Nature("Bold (+Def, -Atk)", Arrays.asList(DECREASED, INCREASED, NORMAL, NORMAL, NORMAL));
    private static final Nature NATURE_BRAVE = new Nature("Brave (+Atk, -Spe)", Arrays.asList(INCREASED, NORMAL, NORMAL, NORMAL, DECREASED));
    private static final Nature NATURE_CALM = new Nature("Calm (+SpD, -Atk)", Arrays.asList(DECREASED, NORMAL, NORMAL, INCREASED, NORMAL));
    private static final Nature NATURE_CAREFUL = new Nature("Careful (+SpD, -SpA)", Arrays.asList(NORMAL, NORMAL, DECREASED, INCREASED, NORMAL));
    private static final Nature NATURE_DOCILE = new Nature("Docile", Arrays.asList(NORMAL, NORMAL, NORMAL, NORMAL, NORMAL));
    private static final Nature NATURE_GENTLE = new Nature("Gentle (+SpD, -Def)", Arrays.asList(NORMAL, DECREASED, NORMAL, INCREASED, NORMAL));
    private static final Nature NATURE_HARDY = new Nature("Hardy", Arrays.asList(NORMAL, NORMAL, NORMAL, NORMAL, NORMAL));
    private static final Nature NATURE_HASTY = new Nature("Hasty (+Spe, -Def)", Arrays.asList(NORMAL, DECREASED, NORMAL, NORMAL, INCREASED));
    private static final Nature NATURE_IMPISH = new Nature("Impish (+Def, -SpA)", Arrays.asList(NORMAL, INCREASED, DECREASED, NORMAL, NORMAL));
    private static final Nature NATURE_JOLLY = new Nature("Jolly (+Spe, -SpA)", Arrays.asList(NORMAL, NORMAL, DECREASED, NORMAL, INCREASED));
    private static final Nature NATURE_LAX = new Nature("Lax (+Def, -SpD)", Arrays.asList(NORMAL, INCREASED, NORMAL, DECREASED, NORMAL));
    private static final Nature NATURE_LONELY = new Nature("Lonely (+Atk, -Def)", Arrays.asList(INCREASED, DECREASED, NORMAL, NORMAL, NORMAL));
    private static final Nature NATURE_MILD = new Nature("Mild (+SpA, -Def)", Arrays.asList(NORMAL, DECREASED, INCREASED, NORMAL, NORMAL));
    private static final Nature NATURE_MODEST = new Nature("Modest (+SpA, -Atk)", Arrays.asList(DECREASED, NORMAL, INCREASED, NORMAL, NORMAL));
    private static final Nature NATURE_NAIVE = new Nature("Naive (+Spe, -SpD)", Arrays.asList(NORMAL, NORMAL, NORMAL, DECREASED, INCREASED));
    private static final Nature NATURE_NAUGHTY = new Nature("Naughty (+Atk, -SpD)", Arrays.asList(INCREASED, NORMAL, NORMAL, DECREASED, NORMAL));
    private static final Nature NATURE_QUIET = new Nature("Quiet (+SpA, -Spe)", Arrays.asList(NORMAL, NORMAL, INCREASED, NORMAL, DECREASED));
    private static final Nature NATURE_QUIRKY = new Nature("Quirky", Arrays.asList(NORMAL, NORMAL, NORMAL, NORMAL, NORMAL));
    private static final Nature NATURE_RASH = new Nature("Rash (+SpA, -SpD)", Arrays.asList(NORMAL, NORMAL, INCREASED, DECREASED, NORMAL));
    private static final Nature NATURE_RELAXED = new Nature("Relaxed (+Def, -Spe)", Arrays.asList(NORMAL, INCREASED, NORMAL, NORMAL, DECREASED));
    private static final Nature NATURE_SASSY = new Nature("Sassy (+SpD, -Spe)", Arrays.asList(NORMAL, NORMAL, NORMAL, INCREASED, DECREASED));
    private static final Nature NATURE_SERIOUS = new Nature("Serious", Arrays.asList(NORMAL, NORMAL, NORMAL, NORMAL, NORMAL));
    private static final Nature NATURE_TIMID = new Nature("Timid (+Spe, -Atk)", Arrays.asList(DECREASED, NORMAL, NORMAL, NORMAL, INCREASED));
    public static final List<Nature> NATURES = Arrays.asList(NATURE_ADAMANT, NATURE_BASHFUL, NATURE_BOLD, NATURE_BRAVE,
            NATURE_CALM, NATURE_CAREFUL, NATURE_DOCILE, NATURE_GENTLE, NATURE_HARDY, NATURE_HASTY,
            NATURE_IMPISH, NATURE_JOLLY, NATURE_LAX, NATURE_LONELY, NATURE_MILD, NATURE_MODEST,
            NATURE_NAIVE, NATURE_NAUGHTY, NATURE_QUIET, NATURE_QUIRKY, NATURE_RASH, NATURE_RELAXED,
            NATURE_SASSY, NATURE_SERIOUS, NATURE_TIMID);

    @Getter private final String name;
    @Getter private final List<Double> statsMultipliers;

    private Nature(String name, List<Double> effects) {
        this.name = name;
        this.statsMultipliers = effects;
    }

    public static Nature getNature(String natureName) {
        for (Nature NATURE : NATURES) {
            if (natureName.equals(NATURE.getName())) {
                return NATURE;
            }
        }
        return null;
    }

    public static Nature getDefaultNature() {
        return NATURE_SERIOUS;
    }
}
