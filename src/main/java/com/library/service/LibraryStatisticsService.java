package com.library.service;

import com.library.repository.BookRepository;
import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.HashMap;

@Service
public class LibraryStatisticsService {
    private final BookRepository bookRepository;
    private final Map<String, String> languageNames;

    public LibraryStatisticsService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
        this.languageNames = new HashMap<>();
        initializeLanguageNames();
    }

    private void initializeLanguageNames() {
        languageNames.put("en", "Inglés");
        languageNames.put("es", "Español");
        languageNames.put("fr", "Francés");
        languageNames.put("de", "Alemán");
        languageNames.put("it", "Italiano");
        languageNames.put("pt", "Portugués");
    }

    public Map<String, Long> getBookCountByLanguage() {
        Map<String, Long> stats = new HashMap<>();

        // Obtener estadísticas solo para los idiomas que nos interesan
        languageNames.keySet().forEach(lang -> {
            long count = bookRepository.countByLanguage(lang);
            if (count > 0) {
                stats.put(lang, count);
            }
        });

        return stats;
    }

    public String getLanguageName(String languageCode) {
        return languageNames.getOrDefault(languageCode, languageCode);
    }
}
