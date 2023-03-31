package app.data_objects;

import static app.constants.PokemonConstants.NATURES;

import java.util.List;

import lombok.Getter;

public class Nature {
    @Getter private final String name;
    @Getter private final List<Double> statsMultipliers;

    public Nature(String name, List<Double> effects) {
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
}
