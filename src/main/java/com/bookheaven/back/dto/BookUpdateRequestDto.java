package com.bookheaven.back.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookUpdateRequestDto {
    private Long id;
    private String title;
    private String author;
    private String publisher;
    private Integer publicationYear;
    private Integer price;
}
