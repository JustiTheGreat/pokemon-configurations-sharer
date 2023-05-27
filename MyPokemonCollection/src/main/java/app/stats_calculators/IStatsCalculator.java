package app.stats_calculators;

import java.util.List;

import app.data_objects.Nature;

public interface IStatsCalculator {

    List<Long> calculateStats(List<Long> base, List<Long> ivs, List<Long> evs, long level, Nature nature);
}
