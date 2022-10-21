package com.thash.demo.controller;

import com.thash.demo.model.Ticker;
import com.thash.demo.service.StorageService;
import com.thash.demo.service.TickerService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.aMapWithSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TickerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    @Autowired
    private TickerService tickerService;

    @MockBean
    @Autowired
    private StorageService storageService;

    @Test
    void handleFileUpload() throws Exception{
        String tickerData = "{\n" +
                "  \"quarter\":\"1\",\n" +
                "  \"stock\":\"AA\",\n" +
                "  \"date\":\"1/14/2011\",\n" +
                "  \"open\":\"$16.71\",\n" +
                "  \"high\":\"$16.71\",\n" +
                "  \"low\":\"$15.64\",\n" +
                "  \"close\":\"$15.97\",\n" +
                "  \"volume\":\"242963398\",\n" +
                "  \"percentChangePrice\":\"-4.42849\",\n" +
                "  \"percentChangeVolumeOverLastWk\":\"1.380223028\",\n" +
                "  \"previousWeeksVolume\":\"239655616\",\n" +
                "  \"nextWeeksOpen\":\"$16.19\",\n" +
                "  \"nextWeeksClose\":\"$15.79\",\n" +
                "  \"percentChangeNextWeeksPrice\":\"-2.47066\",\n" +
                "  \"daysToNextDividend\":\"19\",\n" +
                "  \"percentReturnNextDividend\":\"0.187852\"\n" +
                "}";
        MockMultipartFile tickerFile = new MockMultipartFile("file", null,
                MediaType.MULTIPART_FORM_DATA.toString(), tickerData.getBytes());
        String url = String.format("/api/upload");
        doNothing().when(storageService).store(any(MultipartFile.class));
        when(tickerService.saveTickerData(any(MultipartFile.class))).thenReturn(Boolean.TRUE);
        MvcResult result = mockMvc.perform(multipart(url)
                        .file(tickerFile))
                .andExpect(status().isCreated())
                .andReturn();
    }

    @Test
    void findByTicker() throws Exception{
        String url = String.format("/api/stocks/%s","AA");
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
        when(tickerService.findByTicker(anyString())).thenReturn(tickerList);
        mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].id", Matchers.is(1000)));

    }

    @Test
    void saveTicker() throws Exception{
        String post = "{\n" +
                "  \"quarter\":\"1\",\n" +
                "  \"stock\":\"AA\",\n" +
                "  \"date\":\"1/14/2011\",\n" +
                "  \"open\":\"$16.71\",\n" +
                "  \"high\":\"$16.71\",\n" +
                "  \"low\":\"$15.64\",\n" +
                "  \"close\":\"$15.97\",\n" +
                "  \"volume\":\"242963398\",\n" +
                "  \"percentChangePrice\":\"-4.42849\",\n" +
                "  \"percentChangeVolumeOverLastWk\":\"1.380223028\",\n" +
                "  \"previousWeeksVolume\":\"239655616\",\n" +
                "  \"nextWeeksOpen\":\"$16.19\",\n" +
                "  \"nextWeeksClose\":\"$15.79\",\n" +
                "  \"percentChangeNextWeeksPrice\":\"-2.47066\",\n" +
                "  \"daysToNextDividend\":\"19\",\n" +
                "  \"percentReturnNextDividend\":\"0.187852\"\n" +
                "}";
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
        String url = String.format("/api/save");
        when(tickerService.saveTicker(anyString())).thenReturn(ticker);
        mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(post))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$", aMapWithSize(17)))
                .andExpect(jsonPath("$['id']", Matchers.is(1000)));
    }
}