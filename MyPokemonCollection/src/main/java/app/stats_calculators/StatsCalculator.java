package app.stats_calculators;

import static app.constants.PokemonConstants.NUMBER_OF_STATS;

import java.util.ArrayList;
import java.util.List;

import app.data_objects.Nature;

public class StatsCalculator implements IStatsCalculator {
    @Override
    public List<Long> calculateStats(List<Long> base, List<Long> ivs,
                                     List<Long> evs, long level, Nature nature) {
        List<Long> total = new ArrayList<>(base);
        List<Double> statsMultipliers = nature.getStatsMultipliers();
        for (int i = 0; i < NUMBER_OF_STATS; i++) {
            total.set(i, (long) (i == 0 ?
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
