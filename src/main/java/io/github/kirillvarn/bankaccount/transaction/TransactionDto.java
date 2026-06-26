package io.github.kirillvarn.bankaccount.transaction;

import java.math.BigDecimal;
import java.util.UUID;
import java.sql.Timestamp;


import io.github.kirillvarn.bankaccount.transaction.Transaction.TransactionType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionDto {
    private UUID id;

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @NotNull
    @Positive
    private BigDecimal amount;

    @NotNull
    private UUID accountId;

    private BigDecimal balanceBefore;

    private BigDecimal balanceAfter;

    private Timestamp createdAt;
}
