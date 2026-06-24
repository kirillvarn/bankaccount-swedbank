package io.github.kirillvarn.bankaccount.exchange;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.stereotype.Service;

import io.github.kirillvarn.bankaccount.account.Account;
import io.github.kirillvarn.bankaccount.account.AccountRepository;
import io.github.kirillvarn.bankaccount.money.MoneyExchangeService;
import io.github.kirillvarn.bankaccount.money.MoneyExchangeService.ExchangeResult;
import io.github.kirillvarn.bankaccount.transaction.Transaction;
import io.github.kirillvarn.bankaccount.transaction.TransactionRepository;
import io.github.kirillvarn.bankaccount.transaction.TransactionService;
import io.github.kirillvarn.bankaccount.transaction.Transaction.TransactionType;
import jakarta.transaction.Transactional;

@Service
public class ExchangeService {
    private ExchangeRepository exchangeRepo;
    private TransactionRepository transactionRepository;
    private AccountRepository accountRepository;
    private MoneyExchangeService moneyService;
    private TransactionService transactionService;

    public ExchangeService(TransactionService transactionService, MoneyExchangeService moneyService,
            AccountRepository accountRepository, ExchangeRepository exchangeRepo,
            TransactionRepository transactionRepository) {
        this.moneyService = moneyService;
        this.exchangeRepo = exchangeRepo;
        this.transactionService = transactionService;
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public Exchange create(Exchange exchange, UUID user_id) {
        Account from = exchange.getFromAccount();
        Account to = exchange.getToAccount();
        BigDecimal amount = exchange.getAmount();

        ExchangeResult appliedRateAmount = moneyService.applyRate(from, to, exchange);
        exchange.setExchangeRate(appliedRateAmount.rate());

        Exchange savedExchange = this.exchangeRepo.save(exchange);

        Transaction add = Transaction.builder()
                .account(from)
                .exchange(savedExchange)
                .transactionType(TransactionType.DEB)
                .amount(amount.negate())
                .build();

        Transaction debit = Transaction.builder()
                .account(to)
                .exchange(savedExchange)
                .transactionType(TransactionType.ADD)
                .amount(appliedRateAmount.convertedAmount())
                .build();

        this.transactionService.create(debit, user_id);
        this.transactionService.create(add, user_id);

        return savedExchange;
    }
}
