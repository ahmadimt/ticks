package com.imti.solactive.service;

import com.imti.solactive.model.Response;
import com.imti.solactive.model.Stats;
import com.imti.solactive.model.Tick;
import com.imti.solactive.util.StatsHelper;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * created by imteyaza-1lm on 29/09/19
 **/
@Service
@Slf4j
public class TickServiceImpl implements TickService {

  private final Map<String, Stats> instrumentStats = new ConcurrentHashMap<>();

  private final Stats overallStats = new Stats(new AtomicReference<>(BigDecimal.ZERO),
      new AtomicReference<>(BigDecimal.ZERO), new AtomicReference<>(BigDecimal.ZERO),
      new AtomicLong(0));
  private final Map<String, List<Tick>> allTicks = new ConcurrentHashMap<>();

  @Override
  public Tick save(Tick tick) {
    Stats stats = StatsHelper.updateOverallStats(overallStats, tick);
    log.info("Updated Overall Stats {}", stats);
    Stats instrumentStat = StatsHelper.updateInstrumentsStats(instrumentStats, tick);
    log.info("Updated Instrument Stats {}", instrumentStat);
    List<Tick> tickList = allTicks.get(tick.getInstrument());
    if (Objects.isNull(tickList)) {
      tickList = new ArrayList<>();
    }
    tickList.add(tick);
    allTicks.put(tick.getInstrument(), tickList);
    return tick;
  }

  @Override
  public Response getOverallStatistics() {

    if (overallStats.getCount().get() == 0) {
      return new Response(0, 0, 0, 0);
    }
    return new Response(
        overallStats.getTotal().get()
            .divide(BigDecimal.valueOf(overallStats.getCount().get()), RoundingMode.HALF_UP)
            .doubleValue(), overallStats.getMax().get().doubleValue(),
        overallStats.getMin().get().doubleValue(), overallStats.getCount().get());
  }

  @Override
  public Response getStatisticsByInstrument(String instrument) {
    Stats stats = instrumentStats.get(instrument);
    if (Objects.isNull(stats)) {
      return new Response(0, 0, 0, 0);
    }
    return new Response(
        stats.getTotal().get()
            .divide(BigDecimal.valueOf(stats.getCount().get()), RoundingMode.HALF_UP)
            .doubleValue(), stats.getMax().get().doubleValue(),
        stats.getMin().get().doubleValue(), stats.getCount().get());
  }
}
