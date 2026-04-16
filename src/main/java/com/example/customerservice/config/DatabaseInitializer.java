package com.example.customerservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import java.nio.charset.StandardCharsets;

@Component
public class DatabaseInitializer implements CommandLineRunner {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) throws Exception {
        try {
            // Execute TRANSFER_MONEY procedure
            executeScript("schema.sql");
            
            // Execute PAY_BILL procedure
            executeScript("bill-pay-procedure.sql");
            
            // Execute ADD_MONEY procedure
            executeScript("add-money-procedure.sql");
            
            System.out.println("Database stored procedures initialized successfully");
        } catch (Exception e) {
            System.err.println("Error initializing stored procedures: " + e.getMessage());
            // Don't fail the application startup if procedures already exist
        }
    }

    private void executeScript(String scriptPath) throws Exception {
        ClassPathResource resource = new ClassPathResource(scriptPath);
        String script = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
        
        // Split by procedure delimiter and execute each
        String[] statements = script.split("/");
        for (String statement : statements) {
            String trimmed = statement.trim();
            if (!trimmed.isEmpty()) {
                try {
                    jdbcTemplate.execute(trimmed);
                } catch (Exception e) {
                    // Log but don't fail if procedure already exists
                    System.out.println("Note: " + e.getMessage());
                }
            }
        }
    }
}
