package com.example.books.service.impl;

import com.example.books.dto.BookDto;
import com.example.books.entity.Author;
import com.example.books.entity.Book;
import com.example.books.exception.InvalidInputException;
import com.example.books.exception.ResourceNotFoundException;
import com.example.books.repository.AuthorRepository;
import com.example.books.repository.BookRepository;
import com.example.books.service.BookService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    public BookServiceImpl(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    @Override
    @Transactional
    public Book createBook(BookDto dto) {
        var book = new Book();

        if(dto.author() == null){
            throw new InvalidInputException("Missing author");
        }

        var author = new Author();
        if(dto.author().id() == null){
            author.setAge(dto.author().age());
            author.setName(dto.author().name());
            author.setFollowersNumber(dto.author().followersNumber());
        } else {
            author = authorRepository.findById(dto.author().id())
                    .orElseThrow(() -> new InvalidInputException("Author with id " + dto.author().id() + " not found"));
        }

        book.setTitle(dto.title());
        book.setPublicationDate(dto.publicationDate());
        book.setType(dto.type());
        book.setAuthor(author);

        return bookRepository.save(book);
    }

    @Override
    @Transactional
    public Book updateBook(Long id, BookDto dto) {

        if(dto.author() == null){
            throw new InvalidInputException("Missing author");
        }

        var bookDb = bookRepository.findById(id);

        if(bookDb.isPresent()){
            var bookUpdate = bookDb.get();
            bookUpdate.setTitle(dto.title());
            bookUpdate.setPublicationDate(dto.publicationDate());
            bookUpdate.setType(dto.type());

            if(dto.author().id() != null){
                var author = authorRepository.findById(dto.author().id())
                        .orElseThrow(() -> new InvalidInputException("Author with id " + dto.author().id() + " not found"));
                bookUpdate.setAuthor(author);
            }

            bookRepository.save(bookUpdate);
            return bookUpdate;
        } else {
            throw new ResourceNotFoundException("Book not found with id " + dto.id());
        }
    }

    @Override
    public Book getBookById(Long id) {

        var bookDb = bookRepository.findById(id);

        if(bookDb.isPresent()){
            return bookDb.get();
        } else {
            throw new ResourceNotFoundException("Book not found with id " + id);
        }
    }

    @Override
    public Book getBookByTitle(String title) {
        var bookDb = bookRepository.findByTitle(title);

        if(bookDb.isPresent()){
            return bookDb.get();
        } else {
            throw new ResourceNotFoundException("Book not found with title " + title);
        }
    }

    @Override
    public List<Author> getAuthorsByBookId(List<Long> ids) {
        return bookRepository.findBooksAuthors(ids);
    }

    @Transactional
    @Override
    public void deleteBook(Long id) {
        var bookDb = bookRepository.findById(id);

        if(bookDb.isPresent()){
            bookRepository.deleteById(id);
        } else {
            throw new ResourceNotFoundException("Book not found with id " + id);
        }
    }

    @Override
    public Double calculateRating(Book book) {
        long daysSincePublication = calculateDaysSincePublication(book.getPublicationDate());
        double dateWeight = Math.max(0.5, 100 - daysSincePublication /180.0);

        double authorWeight = calculateAuthorWeight(book.getAuthor());

        double rawRating = dateWeight * authorWeight;

        double normalizedRating = Math.min(10, rawRating / 20.0);
        return Math.max(0, Math.round(normalizedRating * 100) / 100.0);
    }

    private long calculateDaysSincePublication(Date publicationDate) {
        Date today = new Date();
        long differenceInMillis = today.getTime() - publicationDate.getTime();
        return TimeUnit.MILLISECONDS.toDays(differenceInMillis);
    }

    private double calculateAuthorWeight(Author author) {
        Long followers = author.getFollowersNumber();
        if (followers >= 100_000) {
            return 2.0; // Highly popular authors
        } else if (followers >= 10_000) {
            return 1.5; // Moderately popular authors
        } else if (followers >= 1_000) {
            return 1.2; // Less popular authors
        } else {
            return 1.0; // Minimal influence
        }
    }
}
