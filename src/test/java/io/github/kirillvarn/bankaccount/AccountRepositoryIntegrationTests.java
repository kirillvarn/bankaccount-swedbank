package io.github.kirillvarn.bankaccount;

import java.util.List;
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

    @Test
    public void findByIdAndUserIdReturnsAccountWhenMatchExists() {
        User user = TestDataUtil.createUser();
        Account acc = TestDataUtil.createAccount(user);

        repo.save(acc);

        Optional<Account> result = repo.findByIdAndUserId(acc.getId(), user.getId());

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(acc);
    }

    @Test
    public void findByIdAndUserIdReturnsEmptyWhenUserIdDoesNotMatch() {
        User user1 = TestDataUtil.createUser();
        User user2 = TestDataUtil.createUser();

        Account acc = TestDataUtil.createAccount(user1);

        repo.save(acc);

        Optional<Account> result = repo.findByIdAndUserId(acc.getId(), user2.getId());

        assertThat(result).isEmpty();
    }

    @Test
    public void findAllByUserIdReturnsAllAccountsForUser() {
        User user = TestDataUtil.createUser();

        Account acc1 = TestDataUtil.createAccount(user);
        Account acc2 = TestDataUtil.createAccount(user);

        repo.save(acc1);
        repo.save(acc2);

        List<Account> result = repo.findAllByUserId(user.getId());

        assertThat(result)
                .hasSize(2)
                .containsExactlyInAnyOrder(acc1, acc2);
    }

    @Test
    public void findAllByUserIdReturnsEmptyListWhenUserHasNoAccounts() {
        User user = TestDataUtil.createUser();

        List<Account> result = repo.findAllByUserId(user.getId());

        assertThat(result).isEmpty();
    }

    @Test
    public void findByIdAndUserUsernameReturnsAccountWhenMatchExists() {
        User user = TestDataUtil.createUser();
        Account acc = TestDataUtil.createAccount(user);

        repo.save(acc);

        Optional<Account> result = repo.findByIdAndUserUsername(acc.getId(), user.getUsername());

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(acc);
    }

    @Test
    public void findByIdAndUserUsernameReturnsEmptyWhenUsernameDoesNotMatch() {
        User user = TestDataUtil.createUser();
        Account acc = TestDataUtil.createAccount(user);

        repo.save(acc);

        Optional<Account> result = repo.findByIdAndUserUsername(acc.getId(), "wrong-user");

        assertThat(result).isEmpty();
    }

    @Test
    public void findAllByUserUsernameReturnsAllAccountsForUser() {
        User user = TestDataUtil.createUser();

        Account acc1 = TestDataUtil.createAccount(user);
        Account acc2 = TestDataUtil.createAccount(user);

        repo.save(acc1);
        repo.save(acc2);

        List<Account> result = repo.findAllByUserUsername(user.getUsername());

        assertThat(result)
                .hasSize(2)
                .containsExactlyInAnyOrder(acc1, acc2);
    }

    @Test
    public void findAllByUserUsernameReturnsEmptyListWhenUserDoesNotExist() {
        List<Account> result = repo.findAllByUserUsername("missing-user");

        assertThat(result).isEmpty();
    }
}
