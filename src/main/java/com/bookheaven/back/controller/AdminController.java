package com.bookheaven.back.controller;

import com.bookheaven.back.dto.MemberCreateRequestDto;
import com.bookheaven.back.service.AdminService;
import com.bookheaven.back.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/amdin")
@RestController
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;


    /**
     * 사용자 등록(일련번호, 이름, 전화번호, 메모(부서나 기타 정보 등록) 등으로 등록)
     */
    @PostMapping("/members")
    public ResponseEntity<String> createMember(@RequestBody MemberCreateRequestDto request) {
        return null;
    }

    /**
     * 사용자별 대출현황 조회(연체 여부도 표시)
     */
    @GetMapping("/members")
    public ResponseEntity<String> getMemberLoans() {
        return null;
    }

    /**
     * 사용자 수정(일련번호, 이름, 전화번호, 메모(부서나 기타 정보 등록) 등으로 수정)
     */
    @PatchMapping("/members")
    public ResponseEntity<String> updateMember(@RequestBody MemberCreateRequestDto request) {
        return null;
    }

    /**
     * 책 등록
     */
    @PostMapping("/books")
    public ResponseEntity<String> createBook(@RequestBody String string) {
        return null;
    }

    /**
     * 책 조회 (대출 현황 및 이력)
     */
    @GetMapping("/books")
    public ResponseEntity<String> getBook() {
        return null;
    }

    /**
     * 책 수정
     */
    @PatchMapping("/books")
    public ResponseEntity<String> updateBook() {
        return null;
    }

    /**
     * 책 대출 (도서 등록 번호와 사용자 번호로 대출 처리, 대출기간은 15일)
     */
    @PostMapping("/books/loan")
    public ResponseEntity<String> loanBook(@RequestBody String string) {
        return null;
    }

    /**
     * 책 반납 (도서 등록 번호와 사용자 번호로 반납 처리)
     */
    @PostMapping("/books/return")
    public ResponseEntity<String> returnBook(@RequestBody String string) {
        return null;
    }
}
