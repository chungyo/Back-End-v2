package com.mmos.mmos.src.repository;

import com.mmos.mmos.src.domain.entity.UserStudy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserStudyRepository extends JpaRepository<UserStudy, Long> {

    Optional<UserStudy> findUserStudyByStudy_StudyIndexAndUser_UserIndex(Long studyIdx, Long userIdx);

    Optional<List<UserStudy>> findUserStudiesByStudy_StudyIndexAndUserstudyMemberStatus(Long idx, Integer status);

}
