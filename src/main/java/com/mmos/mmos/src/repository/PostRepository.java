package com.mmos.mmos.src.repository;

import com.mmos.mmos.src.domain.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    Optional<List<Post>> findPostsByPostIsNotice(Boolean postIsNotice);
}
