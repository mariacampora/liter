package com.library.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.library.model.Book;
import com.library.model.Author;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import org.springframework.stereotype.Service;

@Service
public class BookApiService {
    private final HttpClientService httpClient;
    private final LibraryPersistenceService persistenceService;
    private final String baseUrl = "https://gutendex.com";
    private final ObjectMapper objectMapper;
    private final List<Book> bookCache = new ArrayList<>();
    private final Set<Author> authorCache = new HashSet<>();

    public BookApiService(HttpClientService httpClient, LibraryPersistenceService persistenceService) {
        this.httpClient = httpClient;
        this.persistenceService = persistenceService;
        this.objectMapper = new ObjectMapper();
    }

    public Book searchBookByTitle(String title) throws Exception {
        String encodedTitle = URLEncoder.encode(title, StandardCharsets.UTF_8);
        String jsonResponse = httpClient.get(baseUrl + "/books/?search=" + encodedTitle);

        JsonNode root = objectMapper.readTree(jsonResponse);
        JsonNode results = root.get("results");

        if (results.size() > 0) {
            Book book = objectMapper.treeToValue(results.get(0), Book.class);
            bookCache.add(book);
            if (!book.getAuthors().isEmpty()) {
                authorCache.add(book.getAuthors().get(0));
                persistenceService.saveBook(book);
            }
            return book;
        }

        return null;
    }

    public List<Book> getAllBooks() {
        return new ArrayList<>(bookCache);
    }

    public List<Book> getBooksByLanguage(String language) {
        return bookCache.stream()
                .filter(book -> language.equalsIgnoreCase(book.getLanguage()))
                .toList();
    }

    public List<Author> getAllAuthors() {
        return new ArrayList<>(authorCache);
    }

    public List<Author> getAuthorsAliveInYear(int year) {
        try {
            return persistenceService.findAuthorsAliveInYear(year);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Año inválido: " + e.getMessage());
        }
    }
}
