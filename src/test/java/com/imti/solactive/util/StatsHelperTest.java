package com.imti.solactive.util;

import com.imti.solactive.model.Stats;
import com.imti.solactive.model.Tick;
import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import org.assertj.core.api.Assertions;
import org.junit.Test;

/**
 * created by imteyaza-1lm on 29/09/19
 **/
public class StatsHelperTest {

  @Test
  public void shouldGiveInstrumentsStatistics() {
    Map<String, Stats> stats = new HashMap<>();
    stats.put("Test1",
        new Stats(new AtomicReference<>(BigDecimal.valueOf(100.5)),
            new AtomicReference<>(BigDecimal.valueOf(12.3)),
            new AtomicReference<>(BigDecimal.valueOf(34.5)),
            new AtomicLong(5)));
    Stats stats1 = StatsHelper.fetchStatisticsByInstrument(stats, "Test1");
    Assertions.assertThat(stats1.getCount().get()).isEqualTo(5);
    Assertions.assertThat(stats1.getMax().get().doubleValue()).isEqualTo(34.5);
    Assertions.assertThat(stats1.getMin().get().doubleValue()).isEqualTo(12.3);
    Assertions.assertThat(stats1.getTotal().get().doubleValue()).isEqualTo(100.5);
  }

  @Test
  public void shouldGiveOverallStats() {
    Map<Long, List<Tick>> ticks = new HashMap<>();
    Tick tick = new Tick("IBM.N", 143.82, Instant.now(Clock.systemDefaultZone()).toEpochMilli());
    ArrayList<Tick> ticks1 = new ArrayList<>();
    ticks1.add(tick);
    Stats stats = StatsHelper.updateOverallStats(ticks);
    Assertions.assertThat(stats).isNotNull();
    Assertions.assertThat(stats.getCount().get()).isEqualTo(1);
    Assertions.assertThat(stats.getMin().get()).isEqualTo(BigDecimal.valueOf(143.82));
  }

  @Test
  public void shouldReturnOnlyNonExpiredTickStats() {
    Map<Long, List<Tick>> ticksWithExpiredTimestamp = new ConcurrentHashMap<>();
    Map<Long, List<Tick>> ticksWithValidTimestamp = new ConcurrentHashMap<>();
    long expiredTime = Instant.now(Clock.systemDefaultZone()).minus(90,
        ChronoUnit.SECONDS).toEpochMilli();
    List<Tick> expiredTicks = new ArrayList<>();
    Tick expired = new Tick("IBM.N", 143.82, expiredTime);
    expiredTicks.add(expired);
    ticksWithExpiredTimestamp.put(expired.getTimestamp(), expiredTicks);

    List<Tick> validTicks = new ArrayList<>();
    Tick validTick = new Tick("IBM.N", 141.67, Instant.now().toEpochMilli());
    validTicks.add(validTick);
    ticksWithValidTimestamp.put(validTick.getTimestamp(), validTicks);

    Stats stats1 = StatsHelper.updateOverallStats(ticksWithExpiredTimestamp);
    Assertions.assertThat(stats1).isNotNull();
    Assertions.assertThat(stats1.getCount().get()).isEqualTo(0);

    Stats stats = StatsHelper.updateOverallStats(ticksWithValidTimestamp);
    Assertions.assertThat(stats).isNotNull();
    Assertions.assertThat(stats.getCount().get()).isEqualTo(1);
    Assertions.assertThat(stats.getMin().get()).isEqualTo(BigDecimal.valueOf(141.67));
    Assertions.assertThat(stats.getMax().get()).isEqualTo(BigDecimal.valueOf(141.67));
  }
}
