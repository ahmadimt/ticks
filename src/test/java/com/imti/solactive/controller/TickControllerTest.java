package com.imti.solactive.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.imti.solactive.model.Response;
import com.imti.solactive.model.Tick;
import com.imti.solactive.service.TickService;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

/**
 * created by imteyaza-1lm on 25/09/19
 **/
@WebMvcTest
@RunWith(SpringRunner.class)
public class TickControllerTest {

  private final ObjectMapper objectMapper = new ObjectMapper();

  @MockBean
  private TickService tickService;

  @Autowired
  private MockMvc mockMvc;

  @Test
  public void shouldCreateTicks() throws Exception {
    Tick tick = new Tick("IBM.N", 143.82, Instant.now().toEpochMilli());
    mockMvc.perform(
        MockMvcRequestBuilders.post("/ticks").content(objectMapper.writeValueAsString(tick))
            .contentType(
                MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(MockMvcResultMatchers.status().isCreated());
  }

  @Test
  public void shouldReturn204() throws Exception {

    Tick tick = new Tick("IBM.N", 143.82,
        Instant.now().minus(70, ChronoUnit.SECONDS).toEpochMilli());
    mockMvc.perform(
        MockMvcRequestBuilders.post("/ticks").content(objectMapper.writeValueAsString(tick))
            .contentType(
                MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(MockMvcResultMatchers.status().isNoContent());
  }

  @Test
  public void shouldReturnOverallStats() throws Exception {

    Response response = new Response(12.3, 34.5, 12.0, 5);
    BDDMockito.given(tickService.getOverallStatistics()).willReturn(response);
    mockMvc.perform(
        MockMvcRequestBuilders.get("/statistics")
            .contentType(
                MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("avg").value(12.3))
        .andExpect(MockMvcResultMatchers.jsonPath("max").value(34.5))
        .andExpect(MockMvcResultMatchers.jsonPath("min").value(12.0))
        .andExpect(MockMvcResultMatchers.jsonPath("count").value(5));
  }

  @Test
  public void shouldReturnInstrumentsStats() throws Exception {

    Response response = new Response(13.8, 23.6, 12.0, 4);
    BDDMockito.given(tickService.getStatisticsByInstrument(ArgumentMatchers.anyString()))
        .willReturn(response);
    mockMvc.perform(
        MockMvcRequestBuilders.get("/statistics/IBM.N")
            .contentType(
                MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("avg").value(13.8))
        .andExpect(MockMvcResultMatchers.jsonPath("max").value(23.6))
        .andExpect(MockMvcResultMatchers.jsonPath("min").value(12.0))
        .andExpect(MockMvcResultMatchers.jsonPath("count").value(4));
  }
}