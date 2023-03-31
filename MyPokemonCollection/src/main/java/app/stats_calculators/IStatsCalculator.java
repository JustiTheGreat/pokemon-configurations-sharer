package app.stats_calculators;

import java.util.List;

import app.data_objects.Nature;

public interface IStatsCalculator {
    List<Integer> calculateStats(List<Integer> base, List<Integer> ivs,
                                 List<Integer> evs, int level, Nature nature);
}
