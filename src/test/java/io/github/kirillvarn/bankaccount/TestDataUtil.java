package io.github.kirillvarn.bankaccount;

import java.math.BigDecimal;

import io.github.kirillvarn.bankaccount.account.Account;
import io.github.kirillvarn.bankaccount.exchange.Exchange;
import io.github.kirillvarn.bankaccount.transaction.Transaction;
import io.github.kirillvarn.bankaccount.user.User;

public class TestDataUtil {
    private TestDataUtil() {
    }

    public static Account createAccount(User user) {
        return Account.builder()
                .currency(Exchange.Currency.USD)
                .balance(new BigDecimal(1000))
                .user(user)
                .build();
    }

    public static User createUser() {
        return User.builder()
                .username("Username")
                .build();
    }

    public static Transaction createTransaction(Account acc) {
        return Transaction.builder()
                .account(acc)
                .transactionType(Transaction.TransactionType.ADD)
                .amount(new BigDecimal(10))
                .balanceBefore(new BigDecimal(100))
                .balanceAfter(new BigDecimal(110))
                .build();
    }
}
