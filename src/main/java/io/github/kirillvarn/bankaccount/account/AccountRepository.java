package io.github.kirillvarn.bankaccount.account;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import io.github.kirillvarn.bankaccount.exchange.Exchange;

@Repository
public interface AccountRepository extends CrudRepository<Account, UUID> {
    public Optional<Account> findByIdAndUserId(UUID id, UUID userId);
    public List<Account> findAllByUserId(UUID userId);
    public Optional<Account> existsByUserIdAndCurrency(UUID id, Exchange.Currency currency);
}