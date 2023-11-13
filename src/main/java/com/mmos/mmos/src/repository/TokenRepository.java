package com.mmos.mmos.src.repository;

import com.mmos.mmos.src.domain.entity.Tokens;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Tokens, Long> {
    Optional<Tokens> findByToken(String token);

    List<Tokens> findAllByUserNameAndExpiredIsFalseAndRevokedIsFalse(String username);
}
