package io.github.kirillvarn.bankaccount;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import io.github.kirillvarn.bankaccount.account.Account;
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

    private TransactionRepository repo;

    @Autowired
    public TransactionRepositoryIntegrationTests(TransactionRepository repo) {
        this.repo = repo;
    }

    @Test
    public void accountCreated() {
        User user = TestDataUtil.createUser();
        Account acc = TestDataUtil.createAccount(user);
        Transaction transaction = TestDataUtil.createTransaction(acc);

        repo.save(transaction);

        Optional<Transaction> result = repo.findById(transaction.getId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(transaction);
    };
    
}
