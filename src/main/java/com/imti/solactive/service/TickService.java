package com.imti.solactive.service;

import com.imti.solactive.model.Response;
import com.imti.solactive.model.Tick;

/**
 * created by imteyaza-1lm on 29/09/19
 **/
public interface TickService {

  Tick save(Tick tick);

  Response getOverallStatistics();

  Response getStatisticsByInstrument(String instrument);
}
