package com.microService.UserService.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class RateLimitConfig {

    @Bean
    public Bucket createBucket() {
        // Define your rate limit parameters here
        Bandwidth limit = Bandwidth.classic(1, Refill.intervally(1, Duration.ofSeconds(60))); // 10 calls per minute
        return Bucket4j.builder()
                .addLimit(limit)
                .build();
    }
}
