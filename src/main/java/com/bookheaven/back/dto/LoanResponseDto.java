package com.bookheaven.back.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class LoanResponseDto {
    private Long memberId;
    private String memberName;
    private Long bookId;
    private String bookName;
    private String bookAuthor;
    private String bookPublisher;
    private LocalDate loanDate;
    private LocalDate returnDate;
    private Boolean overdueStatus; // true면 현 날짜가 returnDate보다 지나 연체된 상태, false면 연체되지않은상태
}
