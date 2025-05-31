package com.bookheaven.back.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseAccessToken {
    private String error;
    private String accessToken;
    private String refreshToken;
}
