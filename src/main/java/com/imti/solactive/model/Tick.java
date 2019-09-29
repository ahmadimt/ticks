package com.imti.solactive.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * created by imteyaza-1lm on 25/09/19
 **/
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Tick {

  @JsonProperty("instrument")
  private String instrument;

  @JsonProperty("price")
  private double price;

  @JsonProperty("timestamp")
  private long timestamp;
}
