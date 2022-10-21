package com.thash.demo.controller;

import com.thash.demo.model.Ticker;
import com.thash.demo.service.StorageService;
import com.thash.demo.service.TickerService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequestMapping("/api")
@RestController
@Log4j2
public class TickerController {

    private final StorageService storageService;
    private final TickerService tickerService;

    @Autowired
    public TickerController(StorageService storageService, TickerService tickerService) {
        this.storageService = storageService;
        this.tickerService = tickerService;
    }


    @RequestMapping(path = "/upload", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> handleFileUpload(@RequestPart("file") MultipartFile file) {
        try {
            storageService.store(file);
            tickerService.saveTickerData(file);
            return ResponseEntity.status(HttpStatus.CREATED).body("UPLOAD SUCCESSFUL");
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @RequestMapping(value = "/stocks/{ticker}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<List<Ticker>> findByTicker(@PathVariable(value = "ticker") String ticker) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(tickerService.findByTicker(ticker));
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(path = "/save", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ResponseEntity<Ticker> saveTicker(@RequestBody final String ticker) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(tickerService.saveTicker(ticker));
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
