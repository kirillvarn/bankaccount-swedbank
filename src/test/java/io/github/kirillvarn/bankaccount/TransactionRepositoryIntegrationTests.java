package io.github.kirillvarn.bankaccount;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import io.github.kirillvarn.bankaccount.account.Account;
import io.github.kirillvarn.bankaccount.account.AccountRepository;
import io.github.kirillvarn.bankaccount.transaction.Transaction;
import io.github.kirillvarn.bankaccount.transaction.TransactionRepository;
import io.github.kirillvarn.bankaccount.user.User;
import jakarta.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
public class TransactionRepositoryIntegrationTests {
    @Autowired
    private TransactionRepository repo;

    @Autowired
    private AccountRepository accRepo;

    @Autowired
    public TransactionRepositoryIntegrationTests(TransactionRepository repo) {
        this.repo = repo;
    }

    @Test
    public void transactionCreated() {
        User user = TestDataUtil.createUser();
        Account acc = TestDataUtil.createAccount(user);
        Transaction transaction = TestDataUtil.createTransaction(acc);

        repo.save(transaction);

        Optional<Transaction> result = repo.findById(transaction.getId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(transaction);
    };

    @Test
    public void findByUserUsernameAndAccountIdReturnsTransactions() {
        User user = TestDataUtil.createUser();
        Account account = TestDataUtil.createAccount(user);

        accRepo.save(account);

        Transaction transaction1 = TestDataUtil.createTransaction(account);
        Transaction transaction2 = TestDataUtil.createTransaction(account);

        repo.save(transaction1);
        repo.save(transaction2);

        List<Transaction> result = repo.findByUserUsernameAndAccountId(
                user.getUsername(),
                account.getId(),
                PageRequest.of(0, 10));

        assertThat(result)
                .hasSize(2)
                .containsExactly(transaction2, transaction1);
    }

    @Test
    public void findByUserUsernameAndAccountIdReturnsEmptyWhenUsernameDoesNotMatch() {
        User user = TestDataUtil.createUser();
        Account account = TestDataUtil.createAccount(user);

        accRepo.save(account);

        Transaction transaction1 = TestDataUtil.createTransaction(account);
        repo.save(transaction1);

        List<Transaction> result = repo.findByUserUsernameAndAccountId(
                "wrong-user",
                account.getId(),
                PageRequest.of(0, 10));

        assertThat(result).isEmpty();
    }

    @Test
    public void findByUserUsernameAndAccountIdReturnsEmptyWhenAccountIdDoesNotMatch() {
        User user = TestDataUtil.createUser();

        Account account1 = TestDataUtil.createAccount(user);
        Account account2 = TestDataUtil.createAccount(user);

        accRepo.save(account1);
        accRepo.save(account2);

        Transaction transaction1 = TestDataUtil.createTransaction(account1);
        repo.save(transaction1);

        List<Transaction> result = repo.findByUserUsernameAndAccountId(
                user.getUsername(),
                account2.getId(),
                PageRequest.of(0, 10));

        assertThat(result).isEmpty();
    }

    @Test
    public void findByUserUsernameAndAccountIdFiltersByAccount() {
        User user = TestDataUtil.createUser();

        Account account1 = TestDataUtil.createAccount(user);
        Account account2 = TestDataUtil.createAccount(user);

        accRepo.save(account1);
        accRepo.save(account2);

        Transaction transaction1 = TestDataUtil.createTransaction(account1);
        Transaction transaction2 = TestDataUtil.createTransaction(account2);

        repo.save(transaction1);
        repo.save(transaction2);

        List<Transaction> result = repo.findByUserUsernameAndAccountId(
                user.getUsername(),
                account1.getId(),
                PageRequest.of(0, 10));

        assertThat(result)
                .hasSize(1)
                .containsExactly(transaction1);
    }
}
