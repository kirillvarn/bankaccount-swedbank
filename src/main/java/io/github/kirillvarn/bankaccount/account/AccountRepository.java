package io.github.kirillvarn.bankaccount.account;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import io.github.kirillvarn.bankaccount.user.User;

@Repository
public interface AccountRepository extends CrudRepository<Account, UUID> {
    public Optional<Account> findByIdAndUserId(UUID id, UUID userId);
    public List<Account> findAllByUserId(UUID userId);
    public Optional<Account> findByIdAndUserUsername(UUID id, String userName);
    public List<Account> findAllByUserUsername(String userName);
    public boolean existsByUser(User user);
}