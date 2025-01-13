package com.library.service;

import com.library.entity.BookEntity;
import com.library.entity.AuthorEntity;
import com.library.model.Book;
import com.library.model.Author;
import com.library.repository.BookRepository;
import com.library.repository.AuthorRepository;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class LibraryAdvancedStatisticsService {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    public LibraryAdvancedStatisticsService(
            BookRepository bookRepository,
            AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    public List<Book> getTop10DownloadedBooks() {
        return bookRepository.findTop10ByOrderByDownloadCountDesc()
                .stream()
                .map(this::convertToBookModel)
                .collect(Collectors.toList());
    }

    public Map<String, DoubleSummaryStatistics> getDownloadStatisticsByLanguage() {
        return bookRepository.findAll().stream()
                .collect(Collectors.groupingBy(
                    BookEntity::getLanguage,
                    Collectors.summarizingDouble(BookEntity::getDownloadCount)
                ));
    }

    public List<Author> searchAuthorsByName(String name) {
        return authorRepository.findByNameContainingIgnoreCase(name)
                .stream()
                .map(this::convertToAuthorModel)
                .collect(Collectors.toList());
    }

    public List<Author> getAuthorsBornInCentury(int century) {
        int startYear = (century - 1) * 100;
        int endYear = startYear + 99;
        return authorRepository.findAuthorsBornBetween(startYear, endYear)
                .stream()
                .map(this::convertToAuthorModel)
                .collect(Collectors.toList());
    }

    public List<Author> getLivingAuthors() {
        return authorRepository.findLivingAuthors()
                .stream()
                .map(this::convertToAuthorModel)
                .collect(Collectors.toList());
    }

    private Book convertToBookModel(BookEntity entity) {
        Book book = new Book();
        book.setId(entity.getGutendexId());
        book.setTitle(entity.getTitle());
        book.setLanguage(entity.getLanguage());
        book.setDownloadCount(entity.getDownloadCount());
        // Convertir autor
        Author author = convertToAuthorModel(entity.getAuthor());
        book.setAuthors(Collections.singletonList(author));
        return book;
    }

    private Author convertToAuthorModel(AuthorEntity entity) {
        Author author = new Author();
        author.setName(entity.getName());
        author.setBirthYear(entity.getBirthYear());
        author.setDeathYear(entity.getDeathYear());
        return author;
    }
}
