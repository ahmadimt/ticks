package com.imti.solactive.util;

import java.time.Clock;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import org.assertj.core.api.Assertions;
import org.junit.Test;

/**
 * created by imteyaza-1lm on 25/09/19
 **/
public class ValidationUtilTest {

  @Test
  public void shouldReturnTrueWhenPassedTimeWithinSpecifiedDuration() {

    Assertions.assertThat(ValidationUtil.isValidTimeStamp(Instant.now(Clock.systemDefaultZone()).toEpochMilli(), 59,
        ChronoUnit.SECONDS)).isTrue();
  }

  @Test
  public void shouldReturnFalseWhenPassedTimeIsNotWithinSpecifiedDuration() {

    Assertions.assertThat(ValidationUtil
        .isValidTimeStamp(Instant.now(Clock.systemDefaultZone()).minus(70, ChronoUnit.SECONDS).toEpochMilli(), 60,
            ChronoUnit.SECONDS)).isFalse();
  }
}