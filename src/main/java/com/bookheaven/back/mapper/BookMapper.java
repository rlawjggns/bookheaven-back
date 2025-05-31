package com.bookheaven.back.mapper;

import com.bookheaven.back.dto.BookSearchResponseDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Mapper
public interface BookMapper {
    List<BookSearchResponseDto> searchBooks(
            @Param("search") String search,
            @Param("sortField") String sortField,
            @Param("sortOrder") String sortOrder,
            @Param("limit") int limit,
            @Param("offset") int offset
    );

    int countBooks(@Param("search") String search);
}