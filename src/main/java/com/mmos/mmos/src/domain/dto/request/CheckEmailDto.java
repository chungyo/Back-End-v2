package com.mmos.mmos.src.domain.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CheckEmailDto {
    private String email;
    private int code;
}
