package com.library.repository;

import com.library.entity.AuthorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface AuthorRepository extends JpaRepository<AuthorEntity, Long> {
    Optional<AuthorEntity> findByName(String name);

    @Query("SELECT a FROM AuthorEntity a WHERE " +
           "(:year >= a.birthYear) AND " +
           "(a.deathYear IS NULL OR :year <= a.deathYear)")
    List<AuthorEntity> findAuthorsAliveInYear(@Param("year") int year);

    List<AuthorEntity> findByNameContainingIgnoreCase(String name);

    @Query("SELECT a FROM AuthorEntity a WHERE " +
           "a.birthYear BETWEEN :startYear AND :endYear")
    List<AuthorEntity> findAuthorsBornBetween(
        @Param("startYear") int startYear,
        @Param("endYear") int endYear
    );

    @Query("SELECT a FROM AuthorEntity a WHERE " +
           "a.deathYear IS NULL")
    List<AuthorEntity> findLivingAuthors();

    @Query("SELECT DISTINCT a FROM AuthorEntity a LEFT JOIN FETCH a.books")
    List<AuthorEntity> findAllWithBooks();
}
