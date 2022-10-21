package com.thash.demo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.thash.demo.model.Ticker;
import com.thash.demo.repository.TickerRepository;
import com.thash.demo.util.IndexParser;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service(value = "tickerService")
@Log4j2
public class TickerServiceImpl implements TickerService{
    private static final String TICKER_SERVICE = "tickerService";
    private final IndexParser indexParser;
    private final TickerRepository tickerRepository;

    @Autowired
    public TickerServiceImpl(IndexParser indexParser, TickerRepository tickerRepository) {
        this.indexParser = indexParser;
        this.tickerRepository = tickerRepository;
    }

    @Override
    public boolean saveTickerData(MultipartFile file) throws IOException {
        List<Ticker> indices = parseFile(file);
        Iterable<Ticker> indexData = tickerRepository.saveAll(indices);
        return !Objects.isNull(indexData) ? true : false;
    }

    @RateLimiter(name = TICKER_SERVICE, fallbackMethod = "rateLimiterFallback")
    @Override
    public List<Ticker> findByTicker(String ticker) {
        return tickerRepository.findIndexDataByStock(ticker);
    }

    public List<Ticker> rateLimiterFallback(String cause, Throwable throwable) {
        log.error("Inside rateLimiterFallback, cause - {} - {}", cause, throwable.getMessage());
        return Collections.emptyList();
    }

    @Bulkhead(name = TICKER_SERVICE, fallbackMethod = "bulkHeadFallback")
    @Override
    public Ticker saveTicker(String tickerJson) throws JsonProcessingException {
        Ticker ticker = convertJsonToTicker(tickerJson);
        return tickerRepository.save(ticker);
    }

    private Ticker convertJsonToTicker(String tickerJson) throws JsonProcessingException {
        Ticker ticker;
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            mapper.registerModule(new JavaTimeModule());
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            ticker = mapper.readValue(tickerJson, Ticker.class);
            log.info(" Index Data : {}", ticker);

        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw e;
        }
        return ticker;
    }

    public Ticker bulkHeadFallback(String tickerJson, Throwable t) throws JsonProcessingException {
        log.error("Inside bulkHeadFallback, cause - {}", t.toString());
        return convertJsonToTicker(tickerJson);
    }

    private List<Ticker> parseFile(MultipartFile file) throws IOException {
        List<Ticker> indices = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            int i = 0;
            while ((line = br.readLine()) != null) {
                if (i == 0) {
                    //Ignore header line
                    i++;
                    continue;
                }
                indices.add(indexParser.parseStringIntoIndexData(line));
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw e;
        }
        return indices;
    }
}
