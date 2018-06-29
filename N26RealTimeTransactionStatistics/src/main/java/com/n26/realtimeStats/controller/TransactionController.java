package com.n26.realtimeStats.controller;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.n26.realtimeStats.model.Transaction;
import com.n26.realtimeStats.service.StatisticService;

/**
 * @author AliSAGIRLIBAS
 * @since 1.0.0 
 */
@RestController
public class TransactionController {

	private static final Logger log = LoggerFactory.getLogger(TransactionController.class);

	@Autowired
	StatisticService statisticService;

	@Value("${n26.settings.MAX_DELAY_IN_SECONDS}")
	private long maxDelayInSeconds;

	@PostMapping("/transactions")
	public ResponseEntity<Transaction> addTransaction(@RequestBody @Valid Transaction transaction) {

		// TODO: can amount be negative ? Ask to leader
		if (transaction.getTimestamp() > System.currentTimeMillis() - (1000L * maxDelayInSeconds)) {
			transaction.setMaxDelayInSeconds(maxDelayInSeconds);
			statisticService.addNewTransaction(transaction);
			log.info("Added new Transaction:" + transaction);
			return new ResponseEntity<>(HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
	}
}
