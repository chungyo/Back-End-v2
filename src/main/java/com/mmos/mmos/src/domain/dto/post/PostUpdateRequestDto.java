package com.mmos.mmos.src.domain.dto.post;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostUpdateRequestDto {
    private String post_title;
    private String post_contents;
    private String post_image;
}
