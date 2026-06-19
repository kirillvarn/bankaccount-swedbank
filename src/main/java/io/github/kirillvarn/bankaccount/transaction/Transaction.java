package io.github.kirillvarn.bankaccount.transaction;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import io.github.kirillvarn.bankaccount.account.Account;
import io.github.kirillvarn.bankaccount.exchange.Exchange;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="account_transactions")
public class Transaction {
    public enum TranscationType { DEB, ADD };

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 3)
    private TranscationType transactionType;

    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 3)
    private Exchange.Currency currency;

    private BigDecimal balanceBefore;

    private BigDecimal balanceAfter;

    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    private Timestamp createdAt;
}