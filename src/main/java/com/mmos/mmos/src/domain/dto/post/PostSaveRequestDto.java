package com.mmos.mmos.src.domain.dto.post;

import lombok.Getter;

@Getter
public class PostSaveRequestDto {

    private String postTitle;
    private String postContents;
    private String postImage;
    private boolean isNotice;
}
