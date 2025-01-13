package com.library.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Book {
    private Integer id;
    private String title;

    @JsonProperty("authors")
    private List<Author> authors;

    @JsonProperty("languages")
    private List<String> languages;

    @JsonProperty("download_count")
    private Integer downloadCount;

    private String language; // Primer idioma de la lista

    public Book() {}

    // Getters
    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return !authors.isEmpty() ? authors.get(0).getName() : "Desconocido";
    }

    public List<Author> getAuthors() {
        return authors;
    }

    public String getLanguage() {
        return language;
    }

    public Integer getDownloadCount() {
        return downloadCount;
    }

    // Setters
    public void setId(Integer id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setLanguages(List<String> languages) {
        this.languages = languages;
        this.language = !languages.isEmpty() ? languages.get(0) : "unknown";
    }

    public void setDownloadCount(Integer downloadCount) {
        this.downloadCount = downloadCount;
    }

    @Override
    public String toString() {
        return "Book{" +
                "title='" + title + '\'' +
                ", author='" + getAuthor() + '\'' +
                ", language='" + language + '\'' +
                ", downloads=" + downloadCount +
                '}';
    }
}
