package io.github.kirillvarn.bankaccount.account;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Set;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import io.github.kirillvarn.bankaccount.exchange.Exchange;
import io.github.kirillvarn.bankaccount.transaction.Transaction;
import io.github.kirillvarn.bankaccount.user.User;
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
@Table(name = "accounts")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 3)
    private Exchange.Currency currency;

    private Boolean isPrimary;
    private String name;

    private BigDecimal balance;

    @OneToMany(mappedBy = "account", fetch = FetchType.LAZY)
    private Set<Transaction> transactions;

    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    private Timestamp createdAt;
}