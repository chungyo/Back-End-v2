package com.mmos.mmos.src.domain.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostSaveRequestDto {

    private String postTitle;
    private String postContents;
    private String postImage;
    private Boolean isNotice;

}
