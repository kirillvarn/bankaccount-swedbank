package io.github.kirillvarn.bankaccount;

import java.util.UUID;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

public class SecurityUtil {
    public static UUID currentUserId() {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        return UUID.fromString(jwt.getClaim("user_id"));
    }
}
