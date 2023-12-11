package com.mmos.mmos.src.domain.dto.response.post;

import com.mmos.mmos.src.domain.entity.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Getter
@NoArgsConstructor
public class PostPageResponseDto {

    private String title;
    private String studyName;
    private String image;
    private Timestamp createdAt;
    private Timestamp updateAt;
    private String contents;
    private String writerName;

    public PostPageResponseDto(Post post) {
        this.title = post.getPostTitle();
        this.studyName = post.getStudy().getStudyName();
        this.image = post.getPostImage();
        this.createdAt = post.getPostCreatedAt();
        this.updateAt = post.getPostUpdatedAt();
        this.contents = post.getPostContents();
        this.writerName = post.getPostWriterName();
    }
}
