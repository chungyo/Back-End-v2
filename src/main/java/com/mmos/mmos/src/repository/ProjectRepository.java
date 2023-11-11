package com.mmos.mmos.src.repository;

import com.mmos.mmos.src.domain.entity.Project;
import com.mmos.mmos.src.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project,Long> {
    Long countByUserAndProjectIsVisibleTrue(User user);
}
