package io.github.kirillvarn.bankaccount.transaction;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

import io.github.kirillvarn.bankaccount.account.Account;
import io.github.kirillvarn.bankaccount.account.AccountRepository;
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
    public Transaction create(Transaction transaction, UUID userId) {
        Account acc = accRepo.findById(transaction.getAccount().getId())
                .orElseThrow(() -> new RuntimeException("Account not found"));

        BigDecimal newBalance = acc.getBalance().add(transaction.getAmount());
        transaction.setAccount(acc);
        transaction.setId(null);
        transaction.setBalanceBefore(acc.getBalance());
        transaction.setBalanceAfter(newBalance);

        acc.setBalance(newBalance);

        return transactionRepo.save(transaction);
    }

    public List<Transaction> getByAccount(UUID userId, UUID accountId) {
        return transactionRepo.findByUserIdAndAccountId(userId, accountId);
    }
}
