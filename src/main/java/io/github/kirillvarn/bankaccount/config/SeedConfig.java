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

            for (UUID userId : userIds) {
                List<UUID> accountIds = accountsByUser.get(userId);

                // choose two different accounts if possible
                UUID acc0 = accountIds.get(0);
                UUID acc1 = accountIds.size() > 1 ? accountIds.get(1) : acc0;

                String fromCur = jdbcTemplate.queryForObject(
                        "SELECT currency FROM accounts WHERE id = ?",
                        String.class, acc0
                );
                String toCur = jdbcTemplate.queryForObject(
                        "SELECT currency FROM accounts WHERE id = ?",
                        String.class, acc1
                );

                // Create one exchange rate row
                UUID exchangeId = UUID.randomUUID();
                BigDecimal rate = new BigDecimal("1.250000"); // example: from->to
                jdbcTemplate.update(
                        "INSERT INTO exchanges(id, from_currency, to_currency, exchange_rate, balance_before, balance_after, created_at) " +
                                "VALUES (?,?,?,?,?,?,?)",
                        exchangeId,
                        fromCur,
                        toCur,
                        rate,
                        new BigDecimal("500.0000"),
                        new BigDecimal("625.0000"),
                        Timestamp.from(Instant.now())
                );

                BigDecimal balAcc0 = getBalance(acc0);
                BigDecimal balAcc1 = getBalance(acc1);

                BigDecimal amountFrom = new BigDecimal("100.0000");
                BigDecimal amountTo = amountFrom.multiply(rate).setScale(4, BigDecimal.ROUND_HALF_UP);

                updateAccountBalanceAndLogTransaction(
                        acc0, exchangeId,
                        "DEB", // your VARCHAR(3)
                        fromCur,
                        amountFrom,
                        balAcc0.subtract(amountFrom)
                );

                if (!acc1.equals(acc0)) {
                    updateAccountBalanceAndLogTransaction(
                            acc1, exchangeId,
                            "ADD",
                            toCur,
                            amountTo,
                            balAcc1.add(amountTo)
                    );
                }
            }
        }

        private BigDecimal getBalance(UUID accountId) {
            return jdbcTemplate.queryForObject(
                    "SELECT balance FROM accounts WHERE id = ?",
                    BigDecimal.class, accountId
            );
        }

        private void updateAccountBalanceAndLogTransaction(
                UUID accountId,
                UUID exchangeId,
                String txType,
                String currency,
                BigDecimal amount,
                BigDecimal newBalance
        ) {
            BigDecimal balanceBefore = getBalance(accountId);

            // Update accounts balance
            jdbcTemplate.update(
                    "UPDATE accounts SET balance = ? WHERE id = ?",
                    newBalance, accountId
            );

            // Insert into account_transactions
            jdbcTemplate.update(
                    "INSERT INTO account_transactions(id, account_id, exchange_id, transaction_type, amount, currency, balance_before, balance_after, created_at) " +
                            "VALUES (?,?,?,?,?,?,?,?,?)",
                    UUID.randomUUID(),
                    accountId,
                    exchangeId,
                    txType,
                    amount,
                    currency,
                    balanceBefore.setScale(4, BigDecimal.ROUND_HALF_UP),
                    newBalance.setScale(4, BigDecimal.ROUND_HALF_UP),
                    Timestamp.from(Instant.now())
            );
        }
    }
}
