package io.github.kirillvarn.bankaccount.account;

import java.math.BigDecimal;

import io.github.kirillvarn.bankaccount.exchange.Exchange;
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
public class AccountDto {
    @NotNull
    @Enumerated(EnumType.STRING)
    private Exchange.Currency currency;
    private Boolean isPrimary;
    private String name;
    private BigDecimal balance;
}
