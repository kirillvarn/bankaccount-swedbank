package io.github.kirillvarn.bankaccount.transaction;

import java.math.BigDecimal;
import java.util.UUID;

import io.github.kirillvarn.bankaccount.exchange.Exchange;
import io.github.kirillvarn.bankaccount.transaction.Transaction.TranscationType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionDto {
    @NotNull
    @Enumerated(EnumType.STRING)
    private Exchange.Currency currency;
    
    @NotNull
    @Enumerated(EnumType.STRING)
    private TranscationType transactionType;

    @NotNull
    private BigDecimal amount;

    @NotNull
    private UUID accountId;

    private BigDecimal balanceBefore;

    private BigDecimal balanceAfter;
}
