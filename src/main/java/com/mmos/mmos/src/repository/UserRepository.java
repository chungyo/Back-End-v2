package com.mmos.mmos.src.repository;

import com.mmos.mmos.src.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
