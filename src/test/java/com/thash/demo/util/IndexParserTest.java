package com.thash.demo.util;

import com.thash.demo.model.Ticker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IndexParserTest {
    IndexParser testClass;
    @BeforeEach
    void setUp() {
        testClass = new IndexParser();
    }
    @Test
    public void parseStringIntoIndexData_WithMissingValues(){
        String line = "1,AA,1/7/2011,$15.82,$16.72,$15.78,$16.42,239655616,3.79267,,,$16.71,$15.97,-4.42849,26,0.182704";
        Ticker returnValue = testClass.parseStringIntoIndexData(line);
        System.out.printf("Ticker : %s", returnValue);
        Ticker ticker = Ticker.builder()
                .id(null)
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
        assertEquals(returnValue, ticker, "Ticker values should be equal");

    }

    @Test
    public void parseStringIntoIndexData_NoMissingValues(){
        String line = "1,AA,1/7/2011,$15.82,$16.72,$15.78,$16.42,239655616,3.79267,12.23,14.23,$16.71,$15.97,-4.42849,26,0.182704";
        Ticker returnValue = testClass.parseStringIntoIndexData(line);

        Ticker ticker = Ticker.builder()
                .id(null)
                .quarter(1)
                .stock("AA")
                .date(LocalDate.of(2011, 1, 7))
                .open("$15.82")
                .high("$16.72")
                .low("$15.78")
                .close("$16.42")
                .volume(new BigDecimal("239655616"))
                .percentChangePrice(new BigDecimal("3.79267"))
                .percentChangeVolumeOverLastWeek(new BigDecimal("12.23"))
                .previousWeeksVolume(new BigDecimal("14.23"))
                .nextWeeksOpen("$16.71")
                .nextWeeksClose("$15.97")
                .percentChangeNextWeeksPrice(new BigDecimal("-4.42849"))
                .daysToNextDividend(new BigDecimal("26"))
                .percentReturnNextDividend(new BigDecimal("0.182704"))
                .build();
        assertEquals(returnValue, ticker, "Ticker values should be equal");

    }
}
