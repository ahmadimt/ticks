package com.imti.solactive.util;

import java.time.Clock;
import java.time.Instant;
import java.time.temporal.TemporalUnit;
import lombok.experimental.UtilityClass;

/**
 * created by imteyaza-1lm on 25/09/19
 **/
@UtilityClass
public class ValidationUtil {

  public static boolean isValidTimeStamp(long timestamp, long validity, TemporalUnit unit) {
    return unit
        .between(Instant.ofEpochMilli(timestamp), Instant.now(Clock.systemDefaultZone()))
        < validity;
  }
}
