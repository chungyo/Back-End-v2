package com.mmos.mmos.src.repository;

import com.mmos.mmos.src.domain.entity.Calendar;
import com.mmos.mmos.src.domain.entity.Plan;
import com.mmos.mmos.src.domain.entity.Planner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PlanRepository extends JpaRepository<Plan, Long> {
    Optional<List<Plan>> findPlansByPlannerAndPlanIsStudy(Planner planner, Boolean isStudy);

    Optional<List<Plan>> findPlansByPlanner(Planner planner);


    Optional<List<Plan>> findPlansByPlanIsVisibleIsTrueAndPlanner_Calendar(Calendar calendar);

    Long countByPlannerAndPlanIsVisibleTrue(Planner planner);

}
