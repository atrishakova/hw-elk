package ru.netology.demo.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.netology.demo.service.LogGenerator;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class LogController {
    private final LogGenerator generator;

    @GetMapping("/generate")
    public ResponseEntity<String> generate(@RequestParam(name = "count", defaultValue = "0") Integer count) {
        log.info("Test request received with count: {}", count);
        generator.generate(count);
        return ResponseEntity.ok("Generated " + count + " log entries!");
    }

    @GetMapping("/test")
    public ResponseEntity<Map<String, Object>> testLogs() {
        log.info("Testing different log levels");

        log.debug("This is a DEBUG message");
        log.info("This is an INFO message");
        log.warn("This is a WARN message");
        log.error("This is an ERROR message");
        log.error("This is an ERROR with exception", new RuntimeException("Test exception"));

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "All log levels tested");
        response.put("timestamp", System.currentTimeMillis());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/log")
    public ResponseEntity<Map<String, String>> customLog(@RequestBody Map<String, String> request) {
        String level = request.getOrDefault("level", "info");
        String message = request.getOrDefault("message", "Default log message");

        switch (level.toLowerCase()) {
            case "trace":
                log.trace(message);
                break;
            case "debug":
                log.debug(message);
                break;
            case "info":
                log.info(message);
                break;
            case "warn":
            case "warning":
                log.warn(message);
                break;
            case "error":
                log.error(message);
                break;
            default:
                log.info("Unknown level: {}, logging as INFO: {}", level, message);
        }

        Map<String, String> response = new HashMap<>();
        response.put("status", "Logged at level: " + level);
        response.put("message", message);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        log.debug("Health check requested");
        Map<String, String> health = new HashMap<>();
        health.put("status", "UP");
        health.put("application", "demo");
        return ResponseEntity.ok(health);
    }
}