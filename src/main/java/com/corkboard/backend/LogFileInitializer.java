package com.corkboard.backend;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class LogFileInitializer {

    private static final Logger logger = LoggerFactory.getLogger(LogFileInitializer.class);
    private static final Path logDir = Paths.get("logs");

    @PostConstruct
    public void init() {
        try {
            // Check if log directory exists, if not create it
            if (Files.notExists(logDir)) {
                Files.createDirectories(logDir);
                logger.info("Created log directory: {}", logDir.toAbsolutePath());
            }
        } catch (IOException e) {
            logger.error("Failed to create log directory", e);
        }
    }
}