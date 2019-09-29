package com.imti.solactive.service;

import com.imti.solactive.model.Response;
import com.imti.solactive.model.Stats;
import com.imti.solactive.model.Tick;
import com.imti.solactive.util.StatsUtil;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * created by imteyaza-1lm on 29/09/19
 **/
@Service
@Slf4j
public class TickServiceImpl implements TickService {

  private final Map<String, Stats> instrumentStats = new ConcurrentHashMap<>();

  private final Stats overallStats = new Stats(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
      0);
  private final Map<String, List<Tick>> allTicks = new HashMap<>();

  @Override
  public Tick save(Tick tick) {
    Stats stats = StatsUtil.updateOverallStats(overallStats, tick);
    log.info("Updated Overall Stats {}", stats);
    Stats instrumentStat = StatsUtil.updateInstrumentsStats(instrumentStats, tick);
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

    if (overallStats.getCount() == 0) {
      return new Response(0, 0, 0, 0);
    }
    return new Response(
        overallStats.getTotal()
            .divide(BigDecimal.valueOf(overallStats.getCount()), RoundingMode.HALF_UP)
            .doubleValue(), overallStats.getMax().doubleValue(),
        overallStats.getMin().doubleValue(), overallStats.getCount());
  }

  @Override
  public Response getStatisticsByInstrument(String instrument) {
    Stats stats = instrumentStats.get(instrument);
    if (Objects.isNull(stats)) {
      return new Response(0, 0, 0, 0);
    }
    return new Response(
        stats.getTotal()
            .divide(BigDecimal.valueOf(stats.getCount()), RoundingMode.HALF_UP)
            .doubleValue(), stats.getMax().doubleValue(),
        stats.getMin().doubleValue(), stats.getCount());
  }
}
