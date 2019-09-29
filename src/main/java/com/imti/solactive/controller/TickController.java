package com.imti.solactive.controller;

import com.imti.solactive.model.Response;
import com.imti.solactive.model.Tick;
import com.imti.solactive.service.TickService;
import com.imti.solactive.util.ValidationUtil;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.time.temporal.ChronoUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * created by imteyaza-1lm on 25/09/19
 **/
@RestController
@Slf4j
public class TickController {

  private final TickService tickService;

  @Value("${tick.validity.period:60}")
  private long validity;

  @Autowired
  public TickController(TickService tickService) {
    this.tickService = tickService;
  }

  @PostMapping(value = "/ticks", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  @ApiResponses(value = {@ApiResponse(code = 204, message = "No Content"),
      @ApiResponse(code = 201, message = "Created")})
  public ResponseEntity createTicks(@RequestBody Tick tick) {
    if (!ValidationUtil.isValidTimeStamp(tick.getTimestamp(), validity, ChronoUnit.SECONDS)) {
      log.info("Validating timestamp received {}", tick.getTimestamp());
      return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    if (StringUtils.isEmpty(tick.getInstrument())) {
      log.info("Validating if instrument name is null or empty {}", tick.getInstrument());
      return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    tickService.save(tick);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @GetMapping(value = "/statistics", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<Response> getStatistics() {
    return ResponseEntity.ok(tickService.getOverallStatistics());
  }

  @GetMapping(value = "/statistics/{instrument_identifier}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<Response> getInstrumentStatistics(
      @PathVariable(value = "instrument_identifier") String instrumentIdentifier) {
    return ResponseEntity.ok(tickService.getStatisticsByInstrument(instrumentIdentifier));
  }
}
