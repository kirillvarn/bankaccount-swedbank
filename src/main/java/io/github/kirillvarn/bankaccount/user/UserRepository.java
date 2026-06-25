package io.github.kirillvarn.bankaccount.user;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, UUID> {
    public Optional<User> findByUsername(String userName);
}
