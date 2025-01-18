package com.example.books.repository;

import com.example.books.entity.Author;
import com.example.books.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findByTitle(String title);

    @Query("SELECT DISTINCT B.author FROM Book B WHERE B.id IN :booksIds")
    List<Author> findBooksAuthors(@Param("booksIds") List<Long> bookIds);
}
