package com.imti.solactive.util;

import com.imti.solactive.model.Stats;
import com.imti.solactive.model.Tick;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
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

  public static Stats updateOverallStats(Stats overallStats, Tick tick) {
    log.info("Calculate and update overall statistics");
    double tickPrice = tick.getPrice();
    doUpdate(overallStats, tickPrice);
    return overallStats;
  }


  public static Stats updateInstrumentsStats(Map<String, Stats> instrumentStats, Tick tick) {
    log.info("Calculate and update Instrument Specific statistics for {}", tick.getInstrument());
    Stats stats = instrumentStats.get(tick.getInstrument());
    double tickPrice = tick.getPrice();
    AtomicReference<BigDecimal> bigDecimal = new AtomicReference<>(
        BigDecimal.valueOf(tickPrice).setScale(2, RoundingMode.HALF_UP));
    if (Objects.nonNull(stats)) {
      doUpdate(stats, tickPrice);
    } else {
      stats = new Stats(bigDecimal, bigDecimal, bigDecimal, new AtomicLong(1));
    }
    instrumentStats.put(tick.getInstrument(), stats);
    return stats;
  }

  private static void doUpdate(Stats stats, double tickPrice) {
    BigDecimal price = BigDecimal.valueOf(tickPrice).setScale(2, RoundingMode.HALF_UP);
    stats.getTotal().getAndSet(price.add(stats.getTotal().get()));

    if (stats.getMax().get().compareTo(BigDecimal.valueOf(tickPrice)) == -1) {
      stats.getMax().getAndSet(price);
    }
    if (stats.getCount().get() == 0) {
      stats.getMin().getAndSet(price);
    } else {
      if (stats.getMin().get().compareTo(BigDecimal.valueOf(tickPrice)) == 1) {
        stats.getMin().getAndSet(price);
      }
    }
    stats.getCount().getAndIncrement();
  }


}
