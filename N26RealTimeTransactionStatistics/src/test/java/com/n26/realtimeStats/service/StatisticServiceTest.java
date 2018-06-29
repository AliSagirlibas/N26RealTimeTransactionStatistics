package com.n26.realtimeStats.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class StatisticServiceTest {

	@Autowired
	StatisticService statisticService; 
	
	@Test
	public void shouldAddMultipleNewTransactionsConcurrently() {
		//statisticService.addNewTransaction(transaction);
		//assertFalse(true);
	}
	
	
	
}
