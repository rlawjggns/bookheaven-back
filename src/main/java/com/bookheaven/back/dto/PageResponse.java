package com.bookheaven.back.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class PageResponse<T> {
    private List<T> content;      // 현재 페이지 데이터 리스트
    private int page;             // 현재 페이지 번호 (1부터 시작)
    private int size;             // 한 페이지당 데이터 개수
    private int totalPages;       // 전체 페이지 수
    private long totalElements;   // 전체 데이터 개수

    public PageResponse(List<T> content, int page, int size, long totalElements) {
        this.content = content;
        this.page = page;
        this.size = size;
        this.totalElements = totalElements;
        // totalPages 계산 (전체 아이템 수 / 페이지 사이즈, 올림 처리)
        this.totalPages = (int) Math.ceil((double) totalElements / size);
    }
}

