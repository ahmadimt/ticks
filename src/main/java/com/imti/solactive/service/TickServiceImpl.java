package com.imti.solactive.service;

import com.imti.solactive.model.Response;
import com.imti.solactive.model.Stats;
import com.imti.solactive.model.Tick;
import com.imti.solactive.util.StatsHelper;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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

  private final Map<String, Map<Long, Stats>> instrumentStats = new ConcurrentHashMap<>();
  private final Map<Long, Stats> overallStatsMap = new ConcurrentHashMap<>();
  private final Map<Long, List<Tick>> allTicks = new ConcurrentHashMap<>();

  @Override
  public Tick save(Tick tick) {

    List<Tick> tickList = allTicks.get(tick.getTimestamp());
    if (Objects.isNull(tickList)) {
      tickList = new ArrayList<>();
    }
    tickList.add(tick);
    allTicks.put(tick.getTimestamp(), tickList);
    overallStatsMap.put(Instant.now().toEpochMilli(), StatsHelper.updateOverallStats(allTicks));
    Stats stats = getStats(tick);
    log.info("Updated Instrument Stats {}", stats);
    return tick;
  }

  @Override
  public Response getOverallStatistics() {

    return overallStatsMap.entrySet().stream()
        .filter(entry -> ChronoUnit.SECONDS
            .between(Instant.ofEpochMilli(entry.getKey()), Instant.now())
            <= 60)
        .max(Entry.comparingByKey()).map(TickServiceImpl::apply)
        .orElse(new Response(0, 0, 0, 0));

  }

  @Override
  public Response getStatisticsByInstrument(String instrument) {

    Map<Long, Stats> values = instrumentStats.get(instrument);
    return values.entrySet().stream()
        .filter(entry -> ChronoUnit.SECONDS
            .between(Instant.ofEpochMilli(entry.getKey()), Instant.now())
            <= 60).max(Entry.comparingByKey()).map(TickServiceImpl::apply)
        .orElse(new Response(0, 0, 0, 0));
  }

  private Stats getStats(Tick tick) {
    Stats stats = StatsHelper.updateTickStats(tick, allTicks);
    Map<Long, Stats> tempInstrumentStats = new ConcurrentHashMap<>();
    tempInstrumentStats.put(Instant.now().toEpochMilli(), stats);
    instrumentStats.put(tick.getInstrument(), tempInstrumentStats);
    return stats;
  }

  private static Response apply(Entry<Long, Stats> entry) {
    Stats stats = entry.getValue();
    if (stats.getCount().get() == 0) {
      return new Response(0, 0, 0, 0);
    }
    return new Response(
        stats.getTotal().get()
            .divide(BigDecimal.valueOf(stats.getCount().get()), RoundingMode.HALF_UP)
            .doubleValue(), stats.getMax().get().doubleValue(),
        stats.getMin().get().doubleValue(), stats.getCount().get());
  }
}
