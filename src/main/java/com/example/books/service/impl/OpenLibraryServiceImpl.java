package com.example.books.service.impl;

import com.example.books.dto.OpenLibAuthor;
import com.example.books.dto.OpenLibBook;
import com.example.books.exception.ResourceNotFoundException;
import com.example.books.service.OpenLibraryService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class OpenLibraryServiceImpl implements OpenLibraryService {

    private static final String OPEN_LIBRARY_API = "https://openlibrary.org/api/books";

    @Override
    public OpenLibBook fetchBookDetails(String isbn) {
        var restTemplate = new RestTemplate();

        // Build the URL with query parameters
        String url = UriComponentsBuilder.fromHttpUrl(OPEN_LIBRARY_API)
                .queryParam("bibkeys", "ISBN:" + isbn)
                .queryParam("jscmd", "data")
                .queryParam("format", "json")
                .toUriString();

        Map<String, Object> response = restTemplate.getForObject(url, Map.class);

        if (response != null && response.containsKey("ISBN:" + isbn)) {
            Map<String, Object> bookData = (Map<String, Object>) response.get("ISBN:" + isbn);
            return mapToBookDto(isbn, bookData);
        } else {
            throw new ResourceNotFoundException("Book not found for ISBN: " + isbn);
        }
    }

    private OpenLibBook mapToBookDto(String isbn, Map<String, Object> bookData) {
        List<OpenLibAuthor> authors = new ArrayList<>();

        if (bookData.containsKey("authors")) {
            var authorsList = (List<Map<String, Object>>) bookData.get("authors");
            authors = authorsList.stream()
                    .map(author -> new OpenLibAuthor((String) author.get("name")))
                    .toList();
        }

        return new OpenLibBook(isbn, (String) bookData.get("publish_date"), (String) bookData.get("title"), authors);
    }
}
