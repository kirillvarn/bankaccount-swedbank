package io.github.kirillvarn.bankaccount.exchange;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import io.github.kirillvarn.bankaccount.account.Account;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "exchanges")
public class Exchange {
    public enum Currency {
        EUR,
        USD,
        SEK,
        GBP,
        VND
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_account_id")
    private Account toAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_account_id")
    private Account fromAccount;

    @NotNull
    private BigDecimal exchangeRate;

    @NotNull
    @Positive
    private BigDecimal amount;

    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    private Timestamp createdAt;
}