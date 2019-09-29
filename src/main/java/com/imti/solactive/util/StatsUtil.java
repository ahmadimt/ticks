package com.imti.solactive.util;

import com.imti.solactive.model.Stats;
import com.imti.solactive.model.Tick;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.LongAccumulator;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * created by imteyaza-1lm on 29/09/19
 **/
@UtilityClass
@Slf4j
public class StatsUtil {


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
    BigDecimal bigDecimal = BigDecimal.valueOf(tickPrice).setScale(2, RoundingMode.HALF_UP);
    if (Objects.nonNull(stats)) {
      doUpdate(stats, tickPrice);
    } else {
      stats = new Stats(bigDecimal, bigDecimal, bigDecimal, 1);
    }
    instrumentStats.put(tick.getInstrument(), stats);
    return stats;
  }

  private static void doUpdate(Stats stats, double tickPrice) {
    BigDecimal price = BigDecimal.valueOf(tickPrice).setScale(2, RoundingMode.HALF_UP);
    stats.setTotal(stats.getTotal().add(price));
    if (stats.getMax().compareTo(BigDecimal.valueOf(tickPrice)) == -1) {
      stats.setMax(price);
    }
    if (stats.getCount() == 0) {
      stats.setMin(price);
    } else {
      if (stats.getMin().compareTo(BigDecimal.valueOf(tickPrice)) == 1) {
        stats.setMin(price);
      }
    }
    LongAccumulator longAccumulator = new LongAccumulator(Long::sum, stats.getCount());
    longAccumulator.accumulate(1);
    stats.setCount(longAccumulator.get());
  }


}
