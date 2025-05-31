package com.bookheaven.back.controller;

import com.bookheaven.back.dto.BookSearchResponseDto;
import com.bookheaven.back.dto.PageResponse;
import com.bookheaven.back.mapper.BookMapper;
import com.bookheaven.back.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final BookMapper bookMapper;

    /**
     *   제목, 저자, 출판사 등으로 검색
     *   출간년, 제목, 저자 등에 대해 오름차순/내림차순 정렬 제공
     *   대출가능/대출불가 표시
     *   리스트는 페이지 제공
     */
    @GetMapping("/books")
    public PageResponse<BookSearchResponseDto> searchBooks(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "title") String sortField,
            @RequestParam(defaultValue = "asc") String sortOrder,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        int offset = (page - 1) * size;
        List<BookSearchResponseDto> books = bookMapper.searchBooks(search, sortField, sortOrder, size, offset);
        int total = bookMapper.countBooks(search);
        return new PageResponse<>(books, page, size, total);
    }
}
