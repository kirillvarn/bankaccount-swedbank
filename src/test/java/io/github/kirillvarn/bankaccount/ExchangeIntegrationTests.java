package io.github.kirillvarn.bankaccount;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import io.github.kirillvarn.bankaccount.account.Account;
import io.github.kirillvarn.bankaccount.account.AccountRepository;
import io.github.kirillvarn.bankaccount.exchange.Exchange;
import io.github.kirillvarn.bankaccount.exchange.ExchangeService;
import io.github.kirillvarn.bankaccount.money.MoneyExchangeService;
import io.github.kirillvarn.bankaccount.money.MoneyExchangeService.ExchangeResult;
import io.github.kirillvarn.bankaccount.transaction.Transaction;
import io.github.kirillvarn.bankaccount.transaction.TransactionRepository;
import io.github.kirillvarn.bankaccount.user.User;
import jakarta.transaction.Transactional;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class ExchangeIntegrationTests {

    @Autowired
    private ExchangeService exchangeService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @MockitoBean
    private MoneyExchangeService moneyExchangeService;

    @Test
    void createsExchangeAndTwoTransactions() {
        User user = TestDataUtil.createUser();
        Account from = accountRepository.save(TestDataUtil.createAccount(user));
        Account to = accountRepository.save(TestDataUtil.createAccount(user));

        Exchange exchange = new Exchange();
        exchange.setFromAccount(from);
        exchange.setToAccount(to);
        exchange.setAmount(new BigDecimal("100.00"));

        when(moneyExchangeService.applyRate(eq(from), eq(to), any()))
            .thenReturn(new ExchangeResult(new BigDecimal("1.10"), new BigDecimal("110.00")));

        Exchange saved = exchangeService.create(exchange);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getExchangeRate()).isEqualByComparingTo("1.10");

        Iterable<Transaction> transactions = transactionRepository.findAll();

        assertThat(transactions).hasSize(2);

        assertThat(transactions)
            .extracting(Transaction::getAmount)
            .containsExactlyInAnyOrder(
                new BigDecimal("100.00"),
                new BigDecimal("110.00")
            );
    }
}
