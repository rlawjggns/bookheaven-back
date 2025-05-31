package com.bookheaven.back.dto;

import lombok.Data;

@Data
public class BookSearchResponseDto {
    private Long id;
    private String title;
    private String author;
    private String publisher;
    private Integer year;
    private Boolean available;
}
