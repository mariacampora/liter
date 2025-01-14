package com.library.repository;

import com.library.entity.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;
import java.util.Map;

public interface BookRepository extends JpaRepository<BookEntity, Long> {
    Optional<BookEntity> findByGutendexId(Integer gutendexId);
    List<BookEntity> findByLanguage(String language);
    Optional<BookEntity> findByTitle(String title);

    @Query("SELECT b FROM BookEntity b ORDER BY b.downloadCount DESC LIMIT 10")
    List<BookEntity> findTop10ByOrderByDownloadCountDesc();

    @Query("SELECT b.language as language, COUNT(b) as count, " +
           "AVG(b.downloadCount) as avgDownloads, " +
           "MAX(b.downloadCount) as maxDownloads, " +
           "MIN(b.downloadCount) as minDownloads " +
           "FROM BookEntity b GROUP BY b.language")
    List<Map<String, Object>> getBookStatistics();

    @Query("SELECT COUNT(b) FROM BookEntity b WHERE b.language = :language")
    long countByLanguage(@Param("language") String language);

    @Query("SELECT b FROM BookEntity b")
    List<BookEntity> findAllBooks();
}
