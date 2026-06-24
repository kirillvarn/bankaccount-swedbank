package io.github.kirillvarn.bankaccount.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;

@Configuration
public class SeedConfig {

    @Value("${app.seed.enabled:true}")
    private boolean enabled;

    @Value("${app.seed.users:5}")
    private int usersCount;

    @Value("${app.seed.accountsPerUser:2}")
    private int accountsPerUser;

    @Bean
    CommandLineRunner seedRunner(JdbcTemplate jdbcTemplate) {
        return args -> {
            if (!enabled) return;
            new DatabaseSeeder(jdbcTemplate, usersCount, accountsPerUser).seedIfEmpty();
        };
    }

    static class DatabaseSeeder {
        private final JdbcTemplate jdbcTemplate;
        private final int usersCount;
        private final int accountsPerUser;

        DatabaseSeeder(JdbcTemplate jdbcTemplate, int usersCount, int accountsPerUser) {
            this.jdbcTemplate = jdbcTemplate;
            this.usersCount = usersCount;
            this.accountsPerUser = accountsPerUser;
        }

        @Transactional
        public void seedIfEmpty() {
            Integer existingUsers = jdbcTemplate.queryForObject(
                    "SELECT COUNT(*) FROM users", Integer.class
            );
            if (existingUsers != null && existingUsers > 0) return;

            List<UUID> userIds = new ArrayList<>();
            for (int i = 0; i < usersCount; i++) {
                UUID userId = UUID.randomUUID();
                String username = "user_" + (i + 1);
                jdbcTemplate.update(
                        "INSERT INTO users(id, username, created_at) VALUES (?,?,?)",
                        userId, username, Timestamp.from(Instant.now())
                );
                userIds.add(userId);
            }

            List<String> currencies = Arrays.asList("USD", "EUR", "GBP", "SEK", "VND");
            Map<UUID, List<UUID>> accountsByUser = new HashMap<>();
            for (UUID userId : userIds) {
                List<UUID> accountIds = new ArrayList<>();

                for (int j = 0; j < accountsPerUser; j++) {
                    UUID accountId = UUID.randomUUID();
                    boolean isPrimary = (j == 0);

                    String currency = currencies.get((j) % currencies.size());
                    BigDecimal initialBalance = new BigDecimal(new BigInteger(5, new Random()));

                    jdbcTemplate.update(
                            "INSERT INTO accounts(id, user_id, currency, name, is_primary, balance, created_at) " +
                                    "VALUES (?,?,?,?,?,?,?)",
                            accountId,
                            userId,
                            currency,
                            (isPrimary ? "Primary" + (j + 1) : "Secondary" + (j + 1)),
                            isPrimary,
                            initialBalance,
                            Timestamp.from(Instant.now())
                    );
                    accountIds.add(accountId);
                }
                accountsByUser.put(userId, accountIds);
            }

        }
    }
}
