package com.thash.demo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.thash.demo.model.Ticker;
import com.thash.demo.repository.TickerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@SpringBootTest
class TickerServiceImplTest {
    @Autowired
    TickerServiceImpl testClass;

    @MockBean
    @Autowired
    private TickerRepository tickerRepository;

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