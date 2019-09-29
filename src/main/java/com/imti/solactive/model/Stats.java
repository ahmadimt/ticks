package com.imti.solactive.model;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * created by imteyaza-1lm on 26/09/19
 **/
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public final class Stats {

  private AtomicReference<BigDecimal> total = new AtomicReference<>(BigDecimal.ZERO);
  private AtomicReference<BigDecimal> min = new AtomicReference<>(BigDecimal.ZERO);
  private AtomicReference<BigDecimal> max = new AtomicReference<>(BigDecimal.ZERO);
  private AtomicLong count = new AtomicLong(0);


}
