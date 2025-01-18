package com.example.books.controller;

import com.example.books.dto.AuthorDto;
import com.example.books.dto.BookDto;
import com.example.books.entity.Author;
import com.example.books.entity.Book;
import com.example.books.exception.ResourceNotFoundException;
import com.example.books.mapper.AuthorMapper;
import com.example.books.mapper.BookMapper;
import com.example.books.service.BookService;
import com.example.books.service.OpenLibraryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(BookController.class)
@ExtendWith(MockitoExtension.class)
class BookControllerTest {

    @MockitoBean
    private BookService bookService;

    @MockitoBean
    private BookMapper bookMapper;

    @MockitoBean
    private AuthorMapper authorMapper;

    @MockitoBean
    private OpenLibraryService openLibraryService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void updateBook_ShouldReturnUpdatedBookDto_WhenValidIdAndDto() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        Long bookId = 1L;
        AuthorDto authorDto = new AuthorDto(1L, "Ali", 35, 1000L);
        BookDto bookDto = new BookDto(1L, "Updated Book Title", new Date(), authorDto, "Novel", 10D);
        Author author = new Author(1L, "Ali", 35, 1000L);
        Book updatedBook = new Book(1L, "Updated Book Title", author, new Date(), "Novel");
        String jsonContent = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(bookDto);

        when(bookService.updateBook(eq(bookId), any(BookDto.class))).thenReturn(updatedBook);
        when(bookMapper.toBookDto(updatedBook)).thenReturn(bookDto);

        mockMvc.perform(put("/api/books/{id}", bookId)
                        .contentType("application/json")
                        .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Book Title"))
                .andExpect(jsonPath("$.author.name").value("Ali"))
                .andExpect(jsonPath("$.type").value("Novel"));

        verify(bookService, times(1)).updateBook(eq(bookId), any(BookDto.class));
        verify(bookMapper, times(1)).toBookDto(updatedBook);
    }

    @Test
    public void updateBook_ShouldReturnNotFound_WhenBookNotExist() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        Long bookId = 1L;
        AuthorDto authorDto = new AuthorDto(1L, "Ali", 35, 1000L);
        BookDto bookDto = new BookDto(1L, "Updated Book Title", new Date(), authorDto, "Novel", 10D);
        String jsonContent = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(bookDto);

        when(bookService.updateBook(eq(bookId), any(BookDto.class))).thenThrow(new ResourceNotFoundException("Book not found"));

        mockMvc.perform(put("/api/books/{id}", bookId)
                        .contentType("application/json")
                        .content(jsonContent))
                .andExpect(status().isNotFound());

        verify(bookService, times(1)).updateBook(eq(bookId), any(BookDto.class));
    }

}