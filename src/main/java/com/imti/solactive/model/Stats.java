package com.imti.solactive.model;

import java.math.BigDecimal;
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

  private BigDecimal total = BigDecimal.ZERO;
  private BigDecimal min = BigDecimal.ZERO;
  private BigDecimal max = BigDecimal.ZERO;
  private long count;


}
