package com.mmos.mmos.src.domain.dto.post;

import com.mmos.mmos.config.HttpResponseStatus;
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

    private Long writerIdx;

    private Timestamp createdAt;

    private Timestamp updatedAt;

    private HttpResponseStatus status;

    public PostResponseDto(Post post, HttpResponseStatus status) {
        this.idx = post.getPostIndex();
        this.title = post.getPostTitle();
        this.contents = post.getPostContents();
        this.image = post.getPostImage();
        this.isNotice = post.getPostIsNotice();
        this.writer = post.getPostWriterName();
        this.writerIdx = post.getPostWriterIndex();
        this.createdAt = post.getPostCreatedAt();
        this.updatedAt = post.getPostUpdatedAt();
        this.status = status;
    }

    public PostResponseDto(Post post) {
        this.idx = post.getPostIndex();
        this.title = post.getPostTitle();
        this.contents = post.getPostContents();
        this.image = post.getPostImage();
        this.isNotice = post.getPostIsNotice();
        this.writer = post.getPostWriterName();
        this.writerIdx = post.getPostWriterIndex();
        this.createdAt = post.getPostCreatedAt();
        this.updatedAt = post.getPostUpdatedAt();
    }

    public PostResponseDto(HttpResponseStatus status) {
        this.status = status;
    }
}
