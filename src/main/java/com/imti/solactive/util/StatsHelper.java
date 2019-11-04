package com.imti.solactive.util;

import com.imti.solactive.model.Stats;
import com.imti.solactive.model.Tick;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * created by imteyaza-1lm on 29/09/19
 **/
@UtilityClass
@Slf4j
public class StatsHelper {


  public static Stats fetchStatisticsByInstrument(Map<String, Stats> instrumentStats,
      String instrument) {
    return instrumentStats.get(instrument);
  }

  public static Stats updateOverallStats(final Map<Long, List<Tick>> ticks) {
    log.info("Calculating and updating overall statistics");
    List<Tick> collect = ticks.entrySet().stream().filter(
        entry -> ChronoUnit.SECONDS
            .between(Instant.ofEpochMilli(entry.getKey()), Instant.now())
            <= 60)
        .flatMap(entry -> entry.getValue().stream()).collect(Collectors.toList());
    return getStats(collect);
  }

  public static Stats updateTickStats(final Tick tick,
      final Map<Long, List<Tick>> ticks) {
    log.info("Calculating and updating overall statistics");
    List<Tick> ticks1 = ticks.entrySet().stream().filter(
        entry -> ChronoUnit.SECONDS
            .between(Instant.ofEpochMilli(entry.getKey()), Instant.now())
            <= 60)
        .flatMap(entry -> entry.getValue().stream())
        .filter(tick1 -> tick1.getInstrument().equalsIgnoreCase(tick.getInstrument()))
        .collect(Collectors.toList());
    return getStats(ticks1);
  }

  private static Stats getStats(List<Tick> ticks) {
    OptionalDouble tempMax = ticks.stream().mapToDouble(Tick::getPrice).max();
    OptionalDouble tempMin = ticks.stream().mapToDouble(Tick::getPrice).min();
    double max = tempMax.orElse(0.00);
    double min = tempMin.orElse(0.00);
    BigDecimal total = ticks.stream().map(tick1 -> BigDecimal.valueOf(tick1.getPrice()))
        .reduce(BigDecimal.ZERO, BigDecimal::add);
    Stats stats = new Stats(new AtomicReference<>(BigDecimal.ZERO),
        new AtomicReference<>(BigDecimal.ZERO), new AtomicReference<>(BigDecimal.ZERO),
        new AtomicLong(0));
    stats.getCount().getAndSet(ticks.size());
    stats.getTotal().getAndSet(total);
    stats.getMax().getAndSet(BigDecimal.valueOf(max));
    stats.getMin().getAndSet(BigDecimal.valueOf(min));
    return stats;
  }
}
