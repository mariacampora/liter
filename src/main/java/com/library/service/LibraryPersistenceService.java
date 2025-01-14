package com.library.service;

import com.library.entity.AuthorEntity;
import com.library.entity.BookEntity;
import com.library.model.Author;
import com.library.model.Book;
import com.library.repository.AuthorRepository;
import com.library.repository.BookRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LibraryPersistenceService {
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    public LibraryPersistenceService(AuthorRepository authorRepository, BookRepository bookRepository) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
    }

    @Transactional
    public void saveBook(Book book) {
        // Verificar si el libro ya existe
        Optional<BookEntity> existingBook = bookRepository.findByGutendexId(book.getId());
        if (existingBook.isPresent()) {
            return; // El libro ya existe
        }

        // Obtener o crear el autor
        Author bookAuthor = book.getAuthors().get(0);
        AuthorEntity authorEntity = authorRepository.findByName(bookAuthor.getName())
                .orElseGet(() -> {
                    AuthorEntity newAuthor = new AuthorEntity(
                            bookAuthor.getName(),
                            bookAuthor.getBirthYear(),
                            bookAuthor.getDeathYear()
                    );
                    return authorRepository.save(newAuthor);
                });

        // Crear y guardar el libro
        BookEntity bookEntity = new BookEntity(
                book.getTitle(),
                authorEntity,
                book.getLanguage(),
                book.getDownloadCount(),
                book.getId()
        );

        bookRepository.save(bookEntity);
    }

    @Transactional(readOnly = true)
    public List<Author> findAuthorsAliveInYear(int year) {
        if (year < 0 || year > 9999) {
            throw new IllegalArgumentException("El año debe estar entre 0 y 9999");
        }

        return authorRepository.findAuthorsAliveInYear(year)
                .stream()
                .map(this::convertToAuthorModel)
                .collect(Collectors.toList());
    }

    private Author convertToAuthorModel(AuthorEntity entity) {
        Author author = new Author();
        author.setName(entity.getName());
        author.setBirthYear(entity.getBirthYear());
        author.setDeathYear(entity.getDeathYear());
        return author;
    }

    @Transactional(readOnly = true)
    public List<BookEntity> findAllBooks() {
        return bookRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<BookEntity> findBooksByLanguage(String language) {
        return bookRepository.findByLanguage(language);
    }

    @Transactional(readOnly = true)
    public List<Author> findAllAuthors() {
        return authorRepository.findAll().stream()
                .map(this::convertToAuthorModel)
                .collect(Collectors.toList());
    }
}
