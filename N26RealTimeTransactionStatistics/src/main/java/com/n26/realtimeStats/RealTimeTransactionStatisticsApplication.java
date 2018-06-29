package com.n26.realtimeStats;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author AliSAGIRLIBAS
 * @since 1.0.0
 * 
 *  AliSAGIRLIBAS NOTES:
 *  1. Guava's queues or custom queues may be used instead of 3 queues.
 *  Current implementation uses more memory to achieve this goal for simplicity and time limit of the project.
 *  2.Interface Based design for Pojo's and Spring Components has been ignored for simplicity
 *  3. Some of the synchronized methods of StatisticService can be written as synchronized blocks
 *  4.Throughput and client count and expected request per second of the services are not known 
 *  nor can be guessed.If they were known,some optimization may be done.
 *  5. Implementation of Custom API Error Messages has been ignored for simplicity 
 *  6. DataTransferObjects (DTOs) for Transactions has been ignored for simplicity  
 *  7. Detailed validations of Transactions has been ignored for simplicity .Used @only valid annotation. 
 *  8. Authentication/Authorization has been ignored for simplicity
 *  9. File Logging/Detailed Logging has been ignored for simplicity  
 *  10. Horizontal Scaling has been ignored for simplicity
	11. Detailed Testing has been ignored for simplicity   
 */
@SpringBootApplication
public class RealTimeTransactionStatisticsApplication {
 
	public static void main(String[] args) {
		SpringApplication.run(RealTimeTransactionStatisticsApplication.class, args);
	}
}
