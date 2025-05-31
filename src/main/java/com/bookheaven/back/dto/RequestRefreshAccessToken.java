package com.bookheaven.back.dto;

import lombok.Data;

@Data
public class RequestRefreshAccessToken {
    private String refreshToken;
}
