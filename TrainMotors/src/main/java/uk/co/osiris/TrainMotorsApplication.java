package uk.co.osiris;

import java.util.concurrent.Executor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@EnableAsync
@Slf4j
public class TrainMotorsApplication {

	public static void main(String[] args) {
		SpringApplication.run(TrainMotorsApplication.class, args);
	}

	@Bean
	  public Executor taskExecutor() {
	    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
	    executor.setCorePoolSize(2);
	    executor.setMaxPoolSize(2);
	    executor.setQueueCapacity(500);
	    executor.setThreadNamePrefix("motor-");
	    executor.initialize();
	    log.info("Setup the thread execution");
	    return executor;
	  }
}
