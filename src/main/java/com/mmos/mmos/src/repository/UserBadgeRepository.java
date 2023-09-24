package com.mmos.mmos.src.repository;

import com.mmos.mmos.src.domain.entity.UserBadge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserBadgeRepository extends JpaRepository<UserBadge, Long> {

    Optional<List<UserBadge>> findUserBadgesByUser_UserIndexAndBadge_BadgePurpose(Long idx, String purpose);

}
