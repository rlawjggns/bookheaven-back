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
     * 사용자 등록
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
     * 사용자 수정
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
     * 책 수정
     */

    /**
     * 책 조회 (대출 현황 및 이력)
     */

    /**
     * 책 대출 (도서 등록 번호와 사용자 번호로 대출 처리, 대출기간은 15일)
     */

    /**
     * 책 반납 (도서 등록 번호와 사용자 번호로 반납 처리)
     */

}
