package com.imti.solactive.util;

import com.imti.solactive.model.Stats;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
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
}
