package com.mmos.mmos.src.repository;

import com.mmos.mmos.src.domain.entity.University;
import com.mmos.mmos.src.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByUserEmail(String email);

    Optional<User> findUserByUserNickname(String nickName);
}
