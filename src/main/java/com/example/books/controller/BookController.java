package com.example.books.controller;

import com.example.books.dto.AuthorDto;
import com.example.books.dto.BookDto;
import com.example.books.dto.OpenLibBook;
import com.example.books.mapper.AuthorMapper;
import com.example.books.mapper.BookMapper;
import com.example.books.service.BookService;
import com.example.books.service.OpenLibraryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/books")
public class BookController {

    private final BookService bookService;
    private final BookMapper bookMapper;
    private final OpenLibraryService openLibraryService;
    private final AuthorMapper authorMapper;

    public BookController(BookService bookService, BookMapper bookMapper, OpenLibraryService openLibraryService, AuthorMapper authorMapper) {
        this.bookService = bookService;
        this.bookMapper = bookMapper;
        this.openLibraryService = openLibraryService;
        this.authorMapper = authorMapper;
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDto> getBookById(@PathVariable("id") Long id) {
        var details = bookService.getBookById(id);
        return ResponseEntity.ok(bookMapper.toBookDto(details));
    }

    @GetMapping("/title/{title}")
    public ResponseEntity<BookDto> getBookByTitle(@PathVariable("title") String title) {
        var details = bookService.getBookByTitle(title);
        return ResponseEntity.ok(bookMapper.toBookDto(details));
    }

    @GetMapping("/isbn/{isbn}")
    public ResponseEntity<OpenLibBook> getBookByISBN(@PathVariable("isbn") String isbn) {
        var details = openLibraryService.fetchBookDetails(isbn);
        return ResponseEntity.ok(details);
    }

    @GetMapping("/authors")
    public ResponseEntity<List<AuthorDto>> getAuthorsByBookIds(@RequestParam("bookIds") List<Long> bookIds) {
        var authors = bookService.getAuthorsByBookId(bookIds);
        return ResponseEntity.ok(authors.stream().map(authorMapper::toAuthorDto).toList());
    }

    @PostMapping
    public ResponseEntity<BookDto> createBook(@Validated @RequestBody BookDto bookDto) {
        var savedBook = bookService.createBook(bookDto);
        return new ResponseEntity<>(bookMapper.toBookDto(savedBook), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookDto> updateBook(@Validated @RequestBody BookDto bookDto, @PathVariable("id") Long id) {
        var bookUpdated = bookService.updateBook(id, bookDto);
        return ResponseEntity.ok(bookMapper.toBookDto(bookUpdated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable("id") Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
}
