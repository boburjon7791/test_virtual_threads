package com.example.test_virtual_threads;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

@EnableScheduling
@SpringBootApplication
public class TestVirtualThreadsApplication {
	int TASK_COUNT=10000;

	/**
	Test cases' results
	Platform Threads Time Taken: 1617 milliseconds
	Virtual Threads Time Taken: 405 milliseconds
	Platform Threads Time Taken: 1589 milliseconds
	Virtual Threads Time Taken: 44 milliseconds
	Platform Threads Time Taken: 1587 milliseconds
	Virtual Threads Time Taken: 45 milliseconds
	Platform Threads Time Taken: 1589 milliseconds
	Virtual Threads Time Taken: 46 milliseconds
	Platform Threads Time Taken: 1585 milliseconds
	Virtual Threads Time Taken: 30 milliseconds
	Platform Threads Time Taken: 1588 milliseconds
	Virtual Threads Time Taken: 29 milliseconds
	Platform Threads Time Taken: 1596 milliseconds
	Virtual Threads Time Taken: 31 milliseconds
	Platform Threads Time Taken: 1592 milliseconds
	Virtual Threads Time Taken: 29 milliseconds
	Virtual Threads Time Taken: 31 milliseconds
	Platform Threads Time Taken: 1586 milliseconds
	 */
	public static void main(String[] args) {
		SpringApplication.run(TestVirtualThreadsApplication.class, args);
	}
	@Scheduled(cron = "0,10,20,30,40,50 * * * * *")
	public void testPlatformThreads() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(100);

        Instant start = Instant.now();

        for (int i = 0; i < TASK_COUNT; i++) {
            executorService.submit(this::performTask);
        }

        executorService.shutdown();
        while (!executorService.isTerminated()) {
            Thread.sleep(10);
        }

        Instant end = Instant.now();
        executorService.close();
        Duration timeElapsed = Duration.between(start, end);
        System.out.println("Platform Threads Time Taken: " + timeElapsed.toMillis() + " milliseconds");
    }
	@Scheduled(cron = "0,10,20,30,40,50 * * * * *")
    public void testVirtualThreads() throws InterruptedException {
        ThreadFactory virtualThreadFactory = Thread.ofVirtual().factory();
        ExecutorService executorService = Executors.newThreadPerTaskExecutor(virtualThreadFactory);

        Instant start = Instant.now();

        for (int i = 0; i < TASK_COUNT; i++) {
            executorService.submit(this::performTask);
        }

        executorService.shutdown();
        while (!executorService.isTerminated()) {
            Thread.sleep(10);
        }

        Instant end = Instant.now();
        executorService.close();
        Duration timeElapsed = Duration.between(start, end);
        System.out.println("Virtual Threads Time Taken: " + timeElapsed.toMillis() + " milliseconds");
    }

    public void performTask() {
        // Simulate a task by sleeping for a short duration
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
