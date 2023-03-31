package app.stats_calculators;

import static app.constants.PokemonConstants.NUMBER_OF_STATS;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import app.data_objects.Nature;

public class StatsCalculator implements IStatsCalculator{
    @Override
    public List<Integer> calculateStats(List<Integer> base, List<Integer> ivs,
                                        List<Integer> evs, int level, Nature nature) {
        List<Integer> total = new ArrayList<>(base);
        List<Double> statsMultipliers = nature.getStatsMultipliers();
        for (int i = 0; i < NUMBER_OF_STATS; i++) {
            total.set(i, (int) (i == 0 ?
                    (2 * total.get(i)
                            + ivs.get(total.indexOf(total.get(i)))
                            + evs.get(total.indexOf(total.get(i))) / 4.0
                    ) * level / 100 + level + 10
                    :
                    ((2 * total.get(i)
                            + ivs.get(total.indexOf(total.get(i)))
                            + evs.get(total.indexOf(total.get(i))) / 4.0
                    ) * level / 100 + 5) * statsMultipliers.get(i - 1)
            ));
        }
        return total;
    }
}
