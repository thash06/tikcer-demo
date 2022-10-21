package com.thash.demo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.thash.demo.model.Ticker;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface TickerService {

    boolean saveTickerData(MultipartFile file) throws IOException;

    List<Ticker> findByTicker(String ticker);

    Ticker saveTicker(String tickerJson) throws JsonProcessingException;

}
