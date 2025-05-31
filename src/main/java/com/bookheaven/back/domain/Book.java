package com.bookheaven.back.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(nullable = false, name = "title")
    private String title;

    @Column(nullable = false, name = "author")
    private String author;

    @Column(nullable = false, name = "publisher")
    private String publisher;

    @Column(nullable = false, name = "publication_year")
    private int publicationYear;

    @Column(nullable = false, name = "price")
    private double price;

    @OneToMany(mappedBy = "book")
    private List<Loan> loans = new ArrayList<>();

    @Builder
    public Book(String title, String author, String publisher, int publicationYear, double price) {
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.publicationYear = publicationYear;
        this.price = price;
    }
}
