package io.github.kirillvarn.bankaccount.account;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import io.github.kirillvarn.bankaccount.exchange.Exchange;
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
@Table(name="accounts")
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

    private BigDecimal balance;

    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    private Timestamp createdAt;
}