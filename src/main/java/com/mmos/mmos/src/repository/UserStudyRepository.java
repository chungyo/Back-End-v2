package com.mmos.mmos.src.repository;

import com.mmos.mmos.src.domain.entity.Study;
import com.mmos.mmos.src.domain.entity.User;
import com.mmos.mmos.src.domain.entity.UserStudy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserStudyRepository extends JpaRepository<UserStudy, Long> {

    Optional<UserStudy> findUserStudyByStudyAndUser(Study study, User user);

    Optional<List<UserStudy>> findUserStudiesByStudy_StudyIndexAndUserstudyMemberStatus(Long idx, Integer status);

}
