package com.n26.realtimeStats.model;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author AliSAGIRLIBAS
 * @since 1.0.0 
 */
public class Transaction implements Delayed {

	@JsonIgnore
	private static AtomicLong atomicId = new AtomicLong(0);

	private long id;
	private double amount;
	private Long timestamp;
	private long maxDelayInSeconds;

	private Transaction() {
		id = this.atomicId.incrementAndGet();
	}

	public Transaction(double amount, Long timestamp) {
		super();
		this.id = this.atomicId.incrementAndGet();
		this.amount = amount;
		this.timestamp = timestamp;
	}

	public long getMaxDelayInSeconds() {
		return maxDelayInSeconds;
	}

	public void setMaxDelayInSeconds(long maxDelayInSeconds) {
		this.maxDelayInSeconds = maxDelayInSeconds;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public int compareTo(Delayed another) {
		Transaction anotherTask = (Transaction) another;

		if (this.timestamp < anotherTask.timestamp) {
			return -1;
		}

		if (this.timestamp > anotherTask.timestamp) {
			return 1;
		}

		return 0;
	}

	@Override
	public long getDelay(TimeUnit unit) {
		long diff = timestamp + (1000L * maxDelayInSeconds) - System.currentTimeMillis();
		return unit.convert(diff, TimeUnit.MILLISECONDS);
	}

	@Override
	public String toString() {
		return "Transaction [id=" + id + ", amount=" + amount + ", Date:"
				+ LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault()) + "]";
	}

}
