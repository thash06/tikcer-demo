package com.thash.demo;

import com.thash.demo.model.IndexData;
import com.thash.demo.model.TickerCurrency;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.StringTokenizer;

public class ParseTickerFile {
    public static void main(String a[]){

        System.out.println(createBigDecimalValue("123"));
        System.out.println(createBigDecimalValue("0.123"));
        System.out.println(createBigDecimalValue(null));
        System.out.println(createBigDecimalValue(""));
        System.out.println(createBigDecimalValue("abc"));

        String path = "/Users/thash06/IdeaProjects/tikcer-demo/src/main/resources/test/dow_jones_index.data";
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            int i=0;
            while ((line = br.readLine()) != null) {
                if(i==0){
                    i++;
                    continue;
                }
                IndexData indexData = createIndexData(line);
                System.out.printf("Line %s\n", indexData);

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private static IndexData createIndexData(String line) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy");
        //LocalDate date = LocalDate.parse("2018-05-05");
        StringTokenizer tokenizer = new StringTokenizer(line, ",");
        String[] columns = line.split(",");
//        String columns[] = new String[16];
//        int i=0;
//        while (tokenizer.hasMoreTokens()){
//            columns[i++] = tokenizer.nextToken();
//        }
        // quarter,stock,date,open,high,low,close,volume,
        // percent_change_price,percent_change_volume_over_last_wk,
        // previous_weeks_volume,next_weeks_open,next_weeks_close,
        // percent_change_next_weeks_price,days_to_next_dividend,percent_return_next_dividend

        IndexData indexData = IndexData.builder()
                .quarter(Integer.parseInt(columns[0]))
                .stock(columns[1])
                .date(LocalDate.parse(columns[2], formatter))
                //TODO keep currency code as well
                .open(createCurrency(columns[3]))
                .high(createCurrency(columns[4]))
                .low(createCurrency(columns[5]))
                .close(createCurrency(columns[6]))

                .volume(createBigDecimalValue(columns[7]))
                .percentChangePrice(createBigDecimalValue(columns[8]))
                .percentChangeVolumeOverLastWeek(createBigDecimalValue(columns[9]))
                .previousWeeksVolume(createBigDecimalValue(columns[10]))
                .nextWeeksOpen(createCurrency(columns[11]))
                .nextWeeksClose(createCurrency(columns[12]))
                .percentChangeNextWeeksPrice(createBigDecimalValue(columns[13]))
                .daysToNextDividend(createBigDecimalValue(columns[14]))
                .percentReturnNextDividend(createBigDecimalValue(columns[15]))
                .build();
        return indexData;
    }

    private static TickerCurrency createTickerCurrency(String value){
        return new TickerCurrency(1000l, Character.toString(value.charAt(0)), new BigDecimal(value.substring(1)));
    }
    private static BigDecimal createCurrency(String value){
        return new BigDecimal(value.substring(1));
    }

    private static BigDecimal createBigDecimalValue(String value){
        if(!StringUtils.hasLength(value)){
            return null;
        }
        try {
            return new BigDecimal(value);
        }catch(Exception e){
            System.err.println(e);
            return null;
        }
    }
}
