package com.imti.solactive.service;

import com.imti.solactive.model.Tick;
import java.time.Clock;
import java.time.Instant;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * created by imteyaza-1lm on 26/09/19
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class TickServiceTest {


  @Autowired
  private TickService tickService;

  @Test
  public void save() {
    long epochMilli = Instant.now(Clock.systemDefaultZone()).toEpochMilli();
    Tick tick = new Tick("IBM.N", 23.45, epochMilli);
    Tick updated = tickService.save(tick);
    Assertions.assertThat(updated.getPrice()).isEqualTo(23.45);
    Assertions.assertThat(updated.getInstrument()).isEqualTo("IBM.N");
    Assertions.assertThat(updated.getTimestamp()).isEqualTo(epochMilli);
  }

  @Configuration
  public static class TestConfiguration {

    @Bean
    public TickService tickService() {
      return new TickServiceImpl();
    }
  }
}