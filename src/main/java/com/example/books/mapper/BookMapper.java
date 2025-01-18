package com.example.books.mapper;

import com.example.books.dto.BookDto;
import com.example.books.entity.Book;
import com.example.books.service.BookService;
import org.springframework.stereotype.Component;

@Component
public class BookMapper {

    private final BookService bookService;
    private final AuthorMapper authorMapper;

    public BookMapper(BookService bookService, AuthorMapper authorMapper) {
        this.bookService = bookService;
        this.authorMapper = authorMapper;
    }

    public BookDto toBookDto(Book book) {
        return BookDto.builder()
                .id(book.getId())
                .title(book.getTitle())
                .publicationDate(book.getPublicationDate())
                .type(book.getType())
                .rating(bookService.calculateRating(book))
                .author(authorMapper.toAuthorDto(book.getAuthor()))
                .build();
    }

    public Book toBook(BookDto bookDto) {
        return Book.builder()
                .id(bookDto.id())
                .type(bookDto.type())
                .title(bookDto.title())
                .publicationDate(bookDto.publicationDate())
                .author(authorMapper.toAuthor(bookDto.author()))
                .build();
    }
}
