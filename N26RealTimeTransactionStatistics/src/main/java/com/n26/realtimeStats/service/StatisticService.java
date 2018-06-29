package com.n26.realtimeStats.service;

import java.util.Comparator;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicLong;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import com.n26.realtimeStats.model.Statistic;
import com.n26.realtimeStats.model.Transaction;

/**
 * @author AliSAGIRLIBAS
 * @since 1.0.0 
 */
@Service
public class StatisticService {

	private static final Logger log = LoggerFactory.getLogger(StatisticService.class);

	private static double sum;		
	private static AtomicLong count = new AtomicLong(0);
	
	private Comparator<Transaction> amountComparatorAsc = new Comparator<Transaction>() {

		@Override
		public int compare(Transaction o1, Transaction o2) {
			return (int) (o1.getAmount() - o2.getAmount());
		}
	};

	private Comparator<Transaction> amountComparatorDesc = new Comparator<Transaction>() {

		@Override
		public int compare(Transaction o1, Transaction o2) {
			return (int) (o2.getAmount() - o1.getAmount());
		}
	};	
	
	//main queue for auto dispatching expired transactions 
	private DelayQueue<Transaction> transactionsSortedByTimeStamp = new DelayQueue<Transaction>();
	
	//transaction queue sorted by amount in ASC order	
	private PriorityBlockingQueue<Transaction> transactionsSortedByAmountAsc = new PriorityBlockingQueue<Transaction>(
			10, amountComparatorAsc);
	
	//transaction queue sorted by amount in DESC order	
	private PriorityBlockingQueue<Transaction> transactionsSortedByAmountDesc = new PriorityBlockingQueue<Transaction>(
			10, amountComparatorDesc);

	//expired TRANSACTION_DATA CLEARER JOB
	@Autowired
	private TaskExecutor taskExecutor;

	public synchronized void addNewTransaction(Transaction transaction) {
		transactionsSortedByAmountAsc.add(transaction);
		transactionsSortedByAmountDesc.add(transaction);
		transactionsSortedByTimeStamp.add(transaction);

		StatisticService.sum = StatisticService.sum + transaction.getAmount();

		long currentSize= count.incrementAndGet();
		log.debug("Queue Size After Add:" +currentSize );

	}

	private synchronized void removeTransaction(Transaction transaction) {

		transactionsSortedByAmountAsc.remove(transaction);
		transactionsSortedByAmountDesc.remove(transaction);
		StatisticService.sum = StatisticService.sum - transaction.getAmount();	
		long currentSize= count.decrementAndGet();
		log.debug("Queue Size After Removal:" +currentSize );
		transaction = null;
	}

	//works in o(1) in a thread safe manner
	//count is pre-calculated and thread safe (atomic)
	//sum is pre-calculated and thread safe (synchronized access)
	//queues are blocking queues and only peeking first elements	
	public synchronized Statistic getStatistics() {
				
		if (count.get() > 0) {
			return new Statistic(StatisticService.sum, StatisticService.sum / count.get(),
					transactionsSortedByAmountDesc.peek().getAmount(),transactionsSortedByAmountAsc.peek().getAmount(), 
					count.get());
		} else {
			return new Statistic(0, 0, 0, 0, 0);
		}

	}

	@PostConstruct
	public void clearExpiredTransactions() {

		taskExecutor.execute(new Runnable() {
			@Override
			public void run() {

				log.info("TRANSACTION_DATA CLEARER JOB HAVE BEEN STARTED\n\n");
				try {
					while (true) {
						if (Thread.interrupted()) {
							break;
						}
						Transaction transaction = transactionsSortedByTimeStamp.take();
						log.info("Removing transaction:" + transaction.toString());						
						removeTransaction(transaction);

					}
				} catch (InterruptedException e) {
					log.warn("TRANSACTION_DATA CLEARER JOB HAD BEEN INTERRUPTED\n\n");
					//e.printStackTrace();
				}
			}
		});
	}

}
