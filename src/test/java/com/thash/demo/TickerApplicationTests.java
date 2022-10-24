package com.thash.demo;

import com.thash.demo.controller.TickerController;
import com.thash.demo.service.TickerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class TickerApplicationTests {
	@Autowired
	private TickerService tickerService;

	@Autowired
	private TickerController tickerController;
	@Test
	void contextLoads() {
		assertThat(tickerController).isNotNull();
		assertThat(tickerService).isNotNull();
	}

}
