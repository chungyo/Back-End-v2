package com.mmos.mmos.src.repository;

import com.mmos.mmos.src.domain.entity.Study;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudyRepository extends JpaRepository<Study, Long> {

//    Optional<List<Study>> findTop3ByStudyIsCompleteAndAndStudyIsVisibleOrderByStudyMemberNumDesc(boolean isComplete, boolean isVisible);

//    Optional<List<Study>> findTop3ByStudyIsCompleteAndStudyIsVisibleOrderByStudyAvgStudyTimeDesc(boolean isComplete, boolean isVisible);
}
