package io.github.kirillvarn.bankaccount.transaction;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends CrudRepository<Transaction, UUID> {
    @Query("""
              select t
              from Transaction t
              where t.account.user.username = :userName
                and t.account.id = :accountId
              order by t.createdAt desc
            """)
    List<Transaction> findByUserUsernameAndAccountId(@Param("userName") String userName,
            @Param("accountId") UUID accountId, Pageable pageable);
}