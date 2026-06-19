package io.github.kirillvarn.bankaccount;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import io.github.kirillvarn.bankaccount.account.Account;
import io.github.kirillvarn.bankaccount.account.AccountRepository;
import io.github.kirillvarn.bankaccount.user.User;
import jakarta.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
public class AccountRepositoryIntegrationTests {

    private AccountRepository repo;

    @Autowired
    public AccountRepositoryIntegrationTests(AccountRepository repo) {
        this.repo = repo;
    }

    @Test
    public void accountCreated() {
        User user = TestDataUtil.createUser();
        Account acc = TestDataUtil.createAccount(user);

        repo.save(acc);

        Optional<Account> result = repo.findById(acc.getId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(acc);
    };
    
}
