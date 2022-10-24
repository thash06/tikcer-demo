package com.thash.demo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.thash.demo.model.Ticker;
import com.thash.demo.repository.TickerRepository;
import com.thash.demo.util.IndexParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
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
    void saveTickerData() throws Exception{
        String fileContents = "quarter,stock,date,open,high,low,close,volume,percent_change_price,percent_change_volume_over_last_wk,previous_weeks_volume,next_weeks_open,next_weeks_close,percent_change_next_weeks_price,days_to_next_dividend,percent_return_next_dividend\n" +
                "1,AA,1/7/2011,$15.82,$16.72,$15.78,$16.42,239655616,3.79267,,,$16.71,$15.97,-4.42849,26,0.182704\n" +
                "1,AA,1/21/2011,$16.19,$16.38,$15.60,$15.79,138428495,-2.47066,-43.02495926,242963398,$15.87,$16.13,1.63831,12,0.189994\n" +
                "1,AA,1/28/2011,$15.87,$16.63,$15.82,$16.13,151379173,1.63831,9.355500109,138428495,$16.18,$17.14,5.93325,5,0.185989\n";
        MultipartFile multipartFile = new MockMultipartFile("file", null,
                MediaType.MULTIPART_FORM_DATA.toString(), fileContents.getBytes());

        assertTrue(testClass.saveTickerData(multipartFile));
    }

    @Test
    void findByTicker() {
        Ticker ticker = Ticker.builder()
                .id(1000l)
                .quarter(1)
                .stock("AA")
                .date(LocalDate.of(2011, 1, 7))
                .open("$15.82")
                .high("$16.72")
                .low("$15.78")
                .close("$16.42")
                .volume(new BigDecimal("239655616"))
                .percentChangePrice(new BigDecimal("3.79267"))
                .nextWeeksOpen("$16.71")
                .nextWeeksClose("$15.97")
                .percentChangeNextWeeksPrice(new BigDecimal("-4.42849"))
                .daysToNextDividend(new BigDecimal("26"))
                .percentReturnNextDividend(new BigDecimal("0.182704"))
                .build();
        List<Ticker> tickerList = new ArrayList<>();
        tickerList.add(ticker);
        when(tickerRepository.findIndexDataByStock(anyString())).thenReturn(tickerList);
        List<Ticker> returnValue = testClass.findByTicker("AA");
        assertEquals(returnValue.size(), 1);
        assertEquals(returnValue.get(0).getId(), 1000l);
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