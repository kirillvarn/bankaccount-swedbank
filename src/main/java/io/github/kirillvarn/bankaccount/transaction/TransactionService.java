package io.github.kirillvarn.bankaccount.transaction;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import io.github.kirillvarn.bankaccount.account.Account;
import io.github.kirillvarn.bankaccount.account.AccountRepository;
import io.github.kirillvarn.bankaccount.transaction.Transaction.TransactionType;
import jakarta.transaction.Transactional;

@Service
public class TransactionService {
    private TransactionRepository transactionRepo;
    private AccountRepository accRepo;

    public TransactionService(TransactionRepository transactionRepo, AccountRepository accRepo) {
        this.transactionRepo = transactionRepo;
        this.accRepo = accRepo;
    }

    @Transactional
    public Transaction create(Transaction transaction) {
        Account acc = accRepo.findById(transaction.getAccount().getId())
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if (transaction.getAmount().compareTo(BigDecimal.ZERO) < 0)
            throw new RuntimeException("Transaction amount should be positive");

        BigDecimal deductibleAmount = transaction.getAmount();

        if (transaction.getTransactionType() == TransactionType.DEB)
            deductibleAmount = deductibleAmount.negate();

        BigDecimal remainingBalance = acc.getBalance().add(deductibleAmount);

        if (remainingBalance.compareTo(BigDecimal.ZERO) < 0)
            throw new RuntimeException("Not enough funds");

        BigDecimal newBalance = acc.getBalance().add(deductibleAmount);
        transaction.setAccount(acc);
        transaction.setId(null);
        transaction.setBalanceBefore(acc.getBalance());
        transaction.setBalanceAfter(newBalance);

        acc.setBalance(newBalance);

        return transactionRepo.save(transaction);
    }

    public List<Transaction> getByAccount(String userName, UUID accountId, int page, int limit) {
        Pageable pageable = PageRequest.of(page, limit);
        return transactionRepo.findByUserUsernameAndAccountId(userName, accountId, pageable);
    }

    public Optional<Transaction> getOne(UUID id) {
        if (id != null)
            return transactionRepo.findById(id);

        return Optional.empty();
    }
}
