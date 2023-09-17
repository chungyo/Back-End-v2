package com.mmos.mmos.src.repository;

import com.mmos.mmos.src.domain.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
