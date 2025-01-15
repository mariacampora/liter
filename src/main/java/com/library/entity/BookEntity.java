package com.library.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "books")
public class BookEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "author_id", nullable = false)
    private AuthorEntity author;

    @Column(nullable = false)
    private String language;

    @Column(name = "download_count")
    private Integer downloadCount;

    @Column(name = "gutendex_id")
    private Integer gutendexId;

    // Constructores
    public BookEntity() {}

    public BookEntity(String title, AuthorEntity author, String language, Integer downloadCount, Integer gutendexId) {
        this.title = title;
        this.author = author;
        this.language = language;
        this.downloadCount = downloadCount;
        this.gutendexId = gutendexId;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public AuthorEntity getAuthor() {
        return author;
    }

    public void setAuthor(AuthorEntity author) {
        this.author = author;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Integer getDownloadCount() {
        return downloadCount;
    }

    public void setDownloadCount(Integer downloadCount) {
        this.downloadCount = downloadCount;
    }

    public Integer getGutendexId() {
        return gutendexId;
    }

    public void setGutendexId(Integer gutendexId) {
        this.gutendexId = gutendexId;
    }
}
