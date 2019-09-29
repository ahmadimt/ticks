package com.imti.solactive.model;

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
public class Response {

  private double avg;
  private double max;
  private double min;
  private long count;
}
