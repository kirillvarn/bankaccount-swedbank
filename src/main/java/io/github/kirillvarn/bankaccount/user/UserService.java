package io.github.kirillvarn.bankaccount.user;

import java.util.Optional;
import java.util.UUID;

public class UserService {
    private UserRepository userRepo;

    public UserService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    public Optional<User> getOne(UUID id) {
        return userRepo.findById(id);
    }
}
