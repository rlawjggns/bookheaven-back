package com.bookheaven.back.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberCreateRequestDto {
    private String name;
    private String tel;
    private String memo;
}
