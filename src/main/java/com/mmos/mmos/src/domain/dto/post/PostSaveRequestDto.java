package com.mmos.mmos.src.domain.dto.post;

import lombok.Getter;

@Getter
public class PostSaveRequestDto {

    private String postTitle;
    private String postContents;
    private String postImage;
    private Boolean isNotice;

    public PostSaveRequestDto(String postTitle, String postContents, String postImage, Boolean isNotice) {
        this.postTitle = postTitle;
        this.postContents = postContents;
        this.postImage = postImage;
        this.isNotice = isNotice;
    }
}
