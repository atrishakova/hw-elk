package ru.netology.demo.service;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.LongStream;

@Slf4j
@Service
public class LogGenerator {

    private final AtomicInteger counter = new AtomicInteger(0);

    public void generate(int count) {
        log.info("Start generating {} logs", count);

        if (count <= 0) {
            log.warn("Count parameter is 0 or negative: {}", count);
            return;
        }

        LongStream.range(0, count)
                .forEach(i -> {
                    int index = counter.incrementAndGet();
                    if (i % 10 == 0) {
                        log.debug("Processing log entry #{} (debug)", index);
                    } else if (i % 25 == 0) {
                        log.warn("Processing log entry #{} (warning)", index);
                    } else if (i % 50 == 0) {
                        log.error("Processing log entry #{} (error)", index);
                    } else {
                        log.info("Processing log entry #{}", index);
                    }

                    // Имитация задержки для разнообразия
                    try {
                        Thread.sleep(i % 5);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        log.error("Interrupted while generating logs", e);
                    }
                });

        log.info("Finished generating {} logs. Total: {}", count, counter.get());
    }

    public void generateWithContext(String context, int count) {
        log.info("Generating {} logs with context: {}", count, context);
        MDC.put("context", context);
        try {
            generate(count);
        } finally {
            MDC.remove("context");
        }
    }

    public void generateException(int count) {
        log.info("Generating {} logs with exceptions", count);
        for (int i = 0; i < count; i++) {
            try {
                if (i % 3 == 0) {
                    throw new RuntimeException("Test exception #" + i);
                }
                log.info("Normal log entry #{}", i);
            } catch (Exception e) {
                log.error("Exception caught while generating log #{}", i, e);
            }
        }
    }
}