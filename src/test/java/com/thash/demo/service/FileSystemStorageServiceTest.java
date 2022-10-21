package com.thash.demo.service;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class FileSystemStorageServiceTest {

    @Autowired
    FileSystemStorageService testClass;

    @Test
    void store_emptyFile() throws Exception{
        MultipartFile tickerFile = Mockito.mock(MultipartFile.class);
        when(tickerFile.isEmpty()).thenReturn(Boolean.TRUE);
        try {
            testClass.store(tickerFile);
            fail();
        }catch (Exception e){
            assertEquals("Failed to store empty file.", e.getMessage());
        }
    }
    @Test
    void store() throws Exception{
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
        MockMultipartFile tickerFile = new MockMultipartFile("file", "upload-dir",
                MediaType.MULTIPART_FORM_DATA.toString(), tickerData.getBytes());
        try {
            testClass.store(tickerFile);
        }catch (Exception e){
            fail();
        }
    }


    @Test
    void store_WrongFolder() throws Exception{
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
        MockMultipartFile tickerFile = new MockMultipartFile("file", "",
                MediaType.MULTIPART_FORM_DATA.toString(), tickerData.getBytes());
        try {
            testClass.store(tickerFile);
            fail();
        }catch (Exception e){
            assertEquals("Cannot store file outside current directory.", e.getMessage());
        }
    }

    @Test
    void init() {
        testClass.init();
    }
}