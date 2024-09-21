package com.example.demo.scheduler;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class HealthCheckScheduler {

    @Value("${health.check.url}")
    private String URL;

    private static final Logger logger = LoggerFactory.getLogger(HealthCheckScheduler.class);

    @Autowired
    private RestTemplate restTemplate;

    @Scheduled(cron = "0 0/3 * * * ?")
    public void checkHealth() {
        boolean success = makeHttpCall();
        logger.info("scheduler invoked to " + URL);
        if (!success) {
            logger.error("Failed to call health endpoint after retrying.");
        }
    }

    private boolean makeHttpCall() {
        int attempts = 0;
        int maxAttempts = 2;  // First attempt + one retry

        while (attempts < maxAttempts) {
            attempts++;
            try {
                ResponseEntity<String> response = restTemplate.getForEntity(URL, String.class);

                if (response.getStatusCode().is2xxSuccessful()) {
                    // Success
                    logger.info("Health endpoint responded successfully.");
                    return true;
                } else {
                    // Non-2xx response
                    logger.warn("Received non-success response: {}", response.getStatusCode());
                }
            } catch (Exception e) {
                logger.error("Attempt {}: Exception occurred while calling health endpoint: {}", attempts, e.getMessage());
            }

            if (attempts < maxAttempts) {
                // Wait before retrying (optional)
                try {
                    Thread.sleep(1000);  // Wait 1 second before retrying
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
                logger.info("Retrying to call health endpoint...");
            }
        }
        return false;
    }
}
