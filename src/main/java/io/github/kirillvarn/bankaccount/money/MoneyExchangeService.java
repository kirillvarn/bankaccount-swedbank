package io.github.kirillvarn.bankaccount.money;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

import org.springframework.stereotype.Service;

import io.github.kirillvarn.bankaccount.account.Account;
import io.github.kirillvarn.bankaccount.exchange.Exchange;
import io.github.kirillvarn.bankaccount.exchange.Exchange.Currency;


@Service
public class MoneyExchangeService {
    public record ExchangeResult(
        BigDecimal rate,
        BigDecimal convertedAmount
    ) {}

    public static final Map<Currency, BigDecimal> DEFAULT_RATES = Map.of(
            Currency.EUR, BigDecimal.ONE,
            Currency.USD, new BigDecimal("1.08"),
            Currency.SEK, new BigDecimal("11.30"),
            Currency.GBP, new BigDecimal("0.85"),
            Currency.VND, new BigDecimal("26800"));

    public ExchangeResult applyRate(Account from, Account to, Exchange exchange) {
        Currency fromCurrency = from.getCurrency();
        Currency toCurreny = to.getCurrency();
        BigDecimal amount = exchange.getAmount();

        BigDecimal fromBase = DEFAULT_RATES.get(fromCurrency);
        BigDecimal toBase = DEFAULT_RATES.get(toCurreny);

        BigDecimal rate = fromBase.divide(toBase, 10, RoundingMode.HALF_UP);
        BigDecimal convertedAmount = amount.multiply(rate).setScale(2, RoundingMode.HALF_UP);

        return new ExchangeResult(rate, convertedAmount);
    }

}
