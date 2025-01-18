package com.example.books.service;

import com.example.books.dto.BookDto;
import com.example.books.entity.Author;
import com.example.books.entity.Book;

import java.util.List;

public interface BookService {

    Book createBook(BookDto bookDto);
    Book updateBook(Long id, BookDto book);
    Book getBookById(Long id);
    Book getBookByTitle(String title);
    List<Author> getAuthorsByBookId(List<Long> ids);
    Double calculateRating(Book book);
    void deleteBook(Long id);
}
