package com.library.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.library.model.Book;
import com.library.entity.BookEntity;
import com.library.model.Author;
import java.util.List;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import org.springframework.stereotype.Service;
import java.util.stream.Collectors;
import java.util.Collections;

@Service
public class BookApiService {
    private final HttpClientService httpClient;
    private final LibraryPersistenceService persistenceService;
    private final String baseUrl = "https://gutendex.com";
    private final ObjectMapper objectMapper;

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
            if (!book.getAuthors().isEmpty()) {
                persistenceService.saveBook(book);
            }
            return book;
        }

        return null;
    }

    public List<Book> getAllBooks() {
        return persistenceService.findAllBooks().stream()
                .map(this::convertToBookModel)
                .collect(Collectors.toList());
    }

    private Book convertToBookModel(BookEntity entity) {
        Book book = new Book();
        book.setId(entity.getGutendexId());
        book.setTitle(entity.getTitle());
        book.setLanguage(entity.getLanguage());
        book.setDownloadCount(entity.getDownloadCount());

        Author author = new Author();
        author.setName(entity.getAuthor().getName());
        author.setBirthYear(entity.getAuthor().getBirthYear());
        author.setDeathYear(entity.getAuthor().getDeathYear());

        book.setAuthors(Collections.singletonList(author));
        return book;
    }

    public List<Book> getBooksByLanguage(String language) {
        return persistenceService.findBooksByLanguage(language).stream()
                .map(this::convertToBookModel)
                .collect(Collectors.toList());
    }

    public List<Author> getAllAuthors() {
        return persistenceService.findAllAuthors();
    }

    public List<Author> getAuthorsAliveInYear(int year) {
        try {
            return persistenceService.findAuthorsAliveInYear(year);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Año inválido: " + e.getMessage());
        }
    }
}
