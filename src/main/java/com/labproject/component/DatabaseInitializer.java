package com.labproject.component;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DatabaseInitializer {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void initializeDatabase() {
        String createFunction = """
                CREATE ALIAS IF NOT EXISTS add_numbers FOR "com.labproject.component.MathUtils.addNumbers";
""";

        jdbcTemplate.execute(createFunction);
    }
}