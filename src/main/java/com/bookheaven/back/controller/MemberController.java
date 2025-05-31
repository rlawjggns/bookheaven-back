package com.bookheaven.back.controller;

import com.bookheaven.back.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    /**
     *   제목, 저자, 출판사 등으로 검색
     *   출간년, 제목, 저자 등에 대해 오름차순/내림차순 정렬 제공
     *   대출가능/대출불가 표시
     *   리스트는 페이지 제공
     */
    @GetMapping("/books")
    public ResponseEntity<String> getString() {
        return null;
    }
}
