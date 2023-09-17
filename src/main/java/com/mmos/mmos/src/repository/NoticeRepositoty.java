package com.mmos.mmos.src.repository;

import com.mmos.mmos.src.domain.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepositoty extends JpaRepository<Notice, Long> {
}
