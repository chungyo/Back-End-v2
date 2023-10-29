package com.mmos.mmos.src.domain.dto.response.study;

import com.mmos.mmos.src.domain.entity.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Getter
@NoArgsConstructor
public class NoticeSectionDto {
    // 인덱스
    private Long idx;
    // 글 제목
    private String title;
    // 글쓴이
    private String writer;
    // 작성 시간
    private Timestamp createdAt;

    public NoticeSectionDto(Post post) {
        this.idx = post.getPostIndex();
        this.title = post.getPostTitle();
        this.writer = post.getPostWriterName();
        this.createdAt = post.getPostCreatedAt();
    }
}
