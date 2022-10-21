package com.thash.demo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.thash.demo.model.Ticker;
import com.thash.demo.repository.TickerRepository;
import com.thash.demo.util.IndexParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class TickerServiceImplTest {

    private TickerService testClass;

    private TickerRepository tickerRepository;
    private IndexParser indexParser;
    @BeforeEach
    void setUp() {
        tickerRepository = Mockito.mock(TickerRepository.class);
        indexParser = new IndexParser();
        testClass = new TickerServiceImpl(indexParser, tickerRepository);
    }

    @Test
    void saveTickerData() {
    }

    @Test
    void findByTicker() {
    }

    @Test
    void rateLimiterFallback() {
    }

    @Test
    void saveTicker() throws JsonProcessingException {
        String tickerData = "{\n" +
                "  \"quarter\":\"1\",\n" +
                "  \"stock\":\"AA\",\n" +
                "  \"date\":\"1/14/2011\",\n" +
                "  \"open\":\"$15.82\",\n" +
                "  \"high\":\"$16.72\"" +
                "}";
        Ticker tickerFromJson = Ticker.builder()
                .quarter(1)
                .stock("AA")
                .date(LocalDate.of(2011, 1, 14))
                .open("$15.82")
                .high("$16.72")
                .build();
        Ticker ticker = Ticker.builder()
                .id(1000l)
                .quarter(1)
                .stock("AA")
                .date(LocalDate.of(2011, 1, 14))
                .open("$15.82")
                .high("$16.72")
                .build();
        when(tickerRepository.save(eq(tickerFromJson))).thenReturn(ticker);
        Ticker returnValue = testClass.saveTicker(tickerData);
        assertEquals(ticker, returnValue);
    }

    @Test
    void bulkHeadFallback() {
    }
}