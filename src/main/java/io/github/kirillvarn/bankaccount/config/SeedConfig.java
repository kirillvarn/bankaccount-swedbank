package io.github.kirillvarn.bankaccount.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;

@Configuration
public class SeedConfig {

    @Value("${app.seed.enabled:true}")
    private boolean enabled;

    @Bean
    CommandLineRunner seedRunner(JdbcTemplate jdbcTemplate) {
        return args -> {
            if (!enabled)
                return;
            new DatabaseSeeder(jdbcTemplate).seed();
        };
    }

    static class DatabaseSeeder {
        private final JdbcTemplate jdbcTemplate;

        DatabaseSeeder(JdbcTemplate jdbcTemplate) {
            this.jdbcTemplate = jdbcTemplate;
        }

        @Transactional
        public void seed() {
            UUID userId = UUID.randomUUID();
            String username = "user";
            jdbcTemplate.update(
                    "INSERT INTO users(id, username, created_at) VALUES (?,?,?)",
                    userId, username, Timestamp.from(Instant.now()));
        }
    }
}
