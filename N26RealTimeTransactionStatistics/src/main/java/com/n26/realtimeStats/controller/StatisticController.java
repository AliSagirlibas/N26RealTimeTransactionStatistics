package com.n26.realtimeStats.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.n26.realtimeStats.model.Statistic;
import com.n26.realtimeStats.service.StatisticService;

/**
 * @author AliSAGIRLIBAS
 * @since 1.0.0 
 */
@RestController
@RequestMapping("/statistics")
public class StatisticController {

	@Autowired
	StatisticService statisticService;

	@GetMapping
	public ResponseEntity<Statistic> getStatistics() {
		return new ResponseEntity<Statistic>(statisticService.getStatistics(), HttpStatus.OK);
	}
}
