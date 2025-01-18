package com.example.books.service;

import com.example.books.dto.OpenLibBook;

public interface OpenLibraryService {
    OpenLibBook fetchBookDetails(String isbn);
}
