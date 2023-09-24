package com.mmos.mmos.src.domain.dto.post;

import com.mmos.mmos.src.domain.entity.Post;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
public class PostResponseDto {

    private Long idx;

    private String title;

    private String contents;

    private String image;

    private Boolean isNotice;

    private String writer;

    private Timestamp createdAt;

    private Timestamp updatedAt;

    public PostResponseDto(Post post) {
        this.idx = post.getPostIndex();
        this.title = post.getPostTitle();
        this.contents = post.getPostContents();
        this.image = post.getPostImage();
        this.isNotice = post.getPostIsNotice();
        this.writer = post.getPostWriter();
        this.createdAt = post.getPostCreatedAt();
        this.updatedAt = post.getPostUpdatedAt();
    }
}
