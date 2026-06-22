package io.github.kirillvarn.bankaccount.transaction;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends CrudRepository<Transaction, UUID> {
    @Query("""
              select t
              from Transaction t
              where t.account.user.id = :userId
                and t.account.id = :accountId
              order by t.createdAt desc
            """)
    List<Transaction> findByUserIdAndAccountId(@Param("userId") UUID userId,
            @Param("accountId") UUID accountId);
}