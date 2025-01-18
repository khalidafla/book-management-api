package com.example.books.service.impl;

import com.example.books.dto.AuthorDto;
import com.example.books.dto.BookDto;
import com.example.books.entity.Author;
import com.example.books.entity.Book;
import com.example.books.exception.InvalidInputException;
import com.example.books.exception.ResourceNotFoundException;
import com.example.books.repository.AuthorRepository;
import com.example.books.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Date;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private BookServiceImpl bookServiceImpl;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testCalculateRating_HighlyPopularAuthor_RecentPublication() {
        var author = new Author();
        var book = new Book();
        author.setName("Famous Author");
        author.setFollowersNumber(120_000L); // Highly popular
        book.setAuthor(author);
        book.setPublicationDate(new Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(30))); // 30 days ago

        double rating = bookServiceImpl.calculateRating(book);

        assertEquals(9.9, rating, 0.1); // Should be capped at 10
    }

    @Test
    void testCalculateRating_ModeratelyPopularAuthor_OlderPublication() {
        var author = new Author();
        var book = new Book();
        author.setName("Moderate Author");
        author.setFollowersNumber(15_000L); // Moderately popular
        book.setAuthor(author);
        book.setPublicationDate(new Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(365))); // 1 year ago

        double rating = bookServiceImpl.calculateRating(book);

        assertTrue(rating > 0 && rating < 10); // Should be lower due to older publication date
    }

    @Test
    void testCalculateRating_LessPopularAuthor_RecentPublication() {
        var author = new Author();
        var book = new Book();
        author.setName("New Author");
        author.setFollowersNumber(800L); // Less popular
        book.setAuthor(author);
        book.setPublicationDate(new Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(15))); // 15 days ago

        double rating = bookServiceImpl.calculateRating(book);

        assertTrue(rating > 0 && rating < 10); // Recent publication but low popularity
    }

    @Test
    void testCalculateRating_MinimalAuthorWeight_OldPublication() {
        var author = new Author();
        var book = new Book();
        author.setName("Unknown Author");
        author.setFollowersNumber(50L); // Minimal influence
        book.setAuthor(author);
        book.setPublicationDate(new Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(1000))); // 1000 days ago

        double rating = bookServiceImpl.calculateRating(book);

        assertEquals(4.7, rating, 0.1); // Very old publication, minimal weight
    }

    @Test
    void testCalculateRating_ZeroFollowers() {
        var author = new Author();
        var book = new Book();
        author.setName("No Followers Author");
        author.setFollowersNumber(0L); // Zero followers
        book.setAuthor(author);
        book.setPublicationDate(new Date()); // Today

        double rating = bookServiceImpl.calculateRating(book);

        assertTrue(rating > 0 && rating < 10); // Should still consider the recent publication date
    }

    @Test
    void updateBook_ShouldThrowInvalidInputException_WhenAuthorIsNull() {
        Long bookId = 1L;
        BookDto bookDto = new BookDto(1L, "Title", new Date(), null, "Novel", 9.0); // Author is null

        InvalidInputException exception = assertThrows(InvalidInputException.class, () -> {
            bookServiceImpl.updateBook(bookId, bookDto);
        });

        assertEquals("Missing author", exception.getMessage());
    }

    @Test
    void updateBook_ShouldUpdateBook_WhenBookExists() {
        Long bookId = 1L;
        AuthorDto authorDto = new AuthorDto(1L, "Ali", 35, 1000L);
        BookDto bookDto = new BookDto(1L, "Updated Title", new Date(), authorDto, "Novel", 9.0);

        Author author = new Author(1L, "Ali", 35, 1000L);
        Book book = new Book(bookId, "Old Title", author, new Date(), "Novel");

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(authorRepository.findById(authorDto.id())).thenReturn(Optional.of(author));

        Book updatedBook = bookServiceImpl.updateBook(bookId, bookDto);

        assertEquals("Updated Title", updatedBook.getTitle());
        assertEquals("Ali", updatedBook.getAuthor().getName());
        verify(bookRepository, times(1)).save(updatedBook);
    }

    @Test
    void updateBook_ShouldThrowInvalidInputException_WhenAuthorNotFound() {

        Long bookId = 1L;
        AuthorDto authorDto = new AuthorDto(999L, "NonExistent Author", 30, 500L); // Invalid author id
        BookDto bookDto = new BookDto(1L, "Updated Title", new Date(), authorDto, "Novel", 9.0);

        Book book = new Book(bookId, "Old Title", null, new Date(), "Novel");

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(authorRepository.findById(authorDto.id())).thenReturn(Optional.empty()); // Author not found

        InvalidInputException exception = assertThrows(InvalidInputException.class, () -> {
            bookServiceImpl.updateBook(bookId, bookDto);
        });

        assertEquals("Author with id 999 not found", exception.getMessage());
    }

    @Test
    void updateBook_ShouldThrowResourceNotFoundException_WhenBookNotFound() {

        Long bookId = 999L; // Non-existing book ID
        AuthorDto authorDto = new AuthorDto(1L, "Ali", 35, 1000L);
        BookDto bookDto = new BookDto(999L, "Non-existent Book", new Date(), authorDto, "Novel", 9.0);

        when(bookRepository.findById(bookId)).thenReturn(Optional.empty()); // Book not found

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            bookServiceImpl.updateBook(bookId, bookDto);
        });

        assertEquals("Book not found with id 999", exception.getMessage());
    }
}