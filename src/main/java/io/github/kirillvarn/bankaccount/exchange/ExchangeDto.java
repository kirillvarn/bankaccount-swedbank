package io.github.kirillvarn.bankaccount.exchange;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExchangeDto {
    private BigDecimal exchangeRate;

    private BigDecimal amount;
}
