package io.github.kirillvarn.bankaccount.exchange;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExchangeRequestDto {
    private UUID fromAccountId;

    private UUID toAccountId;

    private BigDecimal amount;
}
