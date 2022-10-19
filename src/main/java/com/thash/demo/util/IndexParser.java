package com.thash.demo.util;

import com.thash.demo.model.IndexData;
import com.thash.demo.model.Ticker;
import com.thash.demo.model.TickerCurrency;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.StringTokenizer;

@Component
public class IndexParser {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy");
    public Ticker parseStringIntoIndexData(String line) {

        String[] columns = line.split(",");
        // quarter,stock,date,open,high,low,close,volume,
        // percent_change_price,percent_change_volume_over_last_wk,
        // previous_weeks_volume,next_weeks_open,next_weeks_close,
        // percent_change_next_weeks_price,days_to_next_dividend,percent_return_next_dividend

        Ticker ticker = Ticker.builder()
                .quarter(Integer.parseInt(columns[0]))
                .stock(columns[1])
                .date(LocalDate.parse(columns[2], formatter))
                //TODO keep currency code as well
                .open(columns[3])
                .high(columns[4])
                .low(columns[5])
                .close(columns[6])

                .volume(createBigDecimalValue(columns[7]))
                .percentChangePrice(createBigDecimalValue(columns[8]))
                .percentChangeVolumeOverLastWeek(createBigDecimalValue(columns[9]))
                .previousWeeksVolume(createBigDecimalValue(columns[10]))
                .nextWeeksOpen(columns[11])
                .nextWeeksClose(columns[12])
                .percentChangeNextWeeksPrice(createBigDecimalValue(columns[13]))
                .daysToNextDividend(createBigDecimalValue(columns[14]))
                .percentReturnNextDividend(createBigDecimalValue(columns[15]))
                .build();
        return ticker;
    }

    private TickerCurrency createTickerCurrency(String value, Long id){
        return new TickerCurrency(id, Character.toString(value.charAt(0)), new BigDecimal(value.substring(1)));
    }

    private BigDecimal createCurrency(String value){
        return new BigDecimal(value.substring(1));
    }

    private BigDecimal createBigDecimalValue(String value){
        return !StringUtils.hasLength(value) ? null : new BigDecimal(value);
    }
}
