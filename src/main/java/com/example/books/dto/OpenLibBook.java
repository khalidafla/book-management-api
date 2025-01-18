package com.example.books.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record OpenLibBook(
        String isbn,
        String publishDate,
        String title,
        List<OpenLibAuthor> authors
) {
}
