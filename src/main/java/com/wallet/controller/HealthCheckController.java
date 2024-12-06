package com.wallet.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@RestController
public class HealthCheckController {
    
    @GetMapping("/actuator/health")
    public ResponseEntity<Health> health() {
        return ResponseEntity.ok(Health.up().build());
    }
}

@Component
class DatabaseHealthIndicator implements HealthIndicator {
    private final javax.sql.DataSource dataSource;

    public DatabaseHealthIndicator(javax.sql.DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Health health() {
        try (var connection = dataSource.getConnection()) {
            var valid = connection.isValid(1000);
            if (valid) {
                return Health.up().build();
            }
        } catch (Exception e) {
            return Health.down().withException(e).build();
        }
        return Health.down().build();
    }
}