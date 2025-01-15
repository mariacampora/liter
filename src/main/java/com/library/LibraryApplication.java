package com.library;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.library.service.BookApiService;
import com.library.model.Book;
import com.library.model.Author;
import com.library.service.LibraryStatisticsService;
import com.library.service.LibraryAdvancedStatisticsService;

import java.util.Scanner;
import java.util.List;
import java.util.Map;
import java.util.DoubleSummaryStatistics;

@SpringBootApplication
public class LibraryApplication implements CommandLineRunner {
    private final Scanner scanner;
    private final BookApiService bookService;
    private final LibraryStatisticsService statisticsService;
    private final LibraryAdvancedStatisticsService advancedStatsService;

    public LibraryApplication(BookApiService bookService, LibraryStatisticsService statisticsService, LibraryAdvancedStatisticsService advancedStatsService) {
        this.scanner = new Scanner(System.in);
        this.bookService = bookService;
        this.statisticsService = statisticsService;
        this.advancedStatsService = advancedStatsService;
    }

    public static void main(String[] args) {
        SpringApplication.run(LibraryApplication.class, args);
    }

    @Override
    public void run(String... args) {
        System.out.println("\n¡Bienvenido a Literalura!");

        boolean continuar = true;
        while (continuar) {
            try {
                mostrarMenu();
                String input = scanner.nextLine().trim();

                // Validar entrada vacía
                if (input.isEmpty()) {
                    System.out.println("\n[!] Por favor, seleccione una opción.");
                    continue;
                }

                // Validar que sea un número
                if (!input.matches("\\d+")) {
                    System.out.println("\n[!] Por favor, ingrese solo números.");
                    continue;
                }

                int opcion = Integer.parseInt(input);

                // Validar rango de opciones
                if (opcion < 0 || opcion > 10) {
                    System.out.println("\n[!] Opción no válida. Elija entre 0 y 10.");
                    continue;
                }

                switch (opcion) {
                    case 1 -> buscarLibroPorTitulo();
                    case 2 -> listarLibros();
                    case 3 -> buscarLibrosPorIdioma();
                    case 4 -> listarAutores();
                    case 5 -> buscarAutoresVivosEnAnio();
                    case 6 -> mostrarEstadisticas();
                    case 7 -> mostrarTop10();
                    case 8 -> mostrarEstadisticasAvanzadas();
                    case 9 -> buscarAutorPorNombre();
                    case 10 -> explorarAutores();
                    case 0 -> continuar = false;
                    default -> System.out.println("\n[!] Opción no válida.");
                }
            } catch (Exception e) {
                System.out.println("\n[X] Error inesperado: " + e.getMessage());
                System.out.println("Por favor, intente nuevamente.");
            }

            if (continuar) {
                System.out.println("\nPresione ENTER para continuar o escriba 'salir' para terminar...");
                String respuesta = scanner.nextLine().trim().toLowerCase();
                if (respuesta.equals("salir")) {
                    continuar = false;
                }
            }
        }

        System.out.println("\n¡Gracias por usar Literalura! [*]");
        scanner.close();
    }

    private void mostrarMenu() {
        System.out.println("\n*** MENÚ PRINCIPAL ***");
        System.out.println("1. Buscar libro por título");
        System.out.println("2. Listar todos los libros");
        System.out.println("3. Buscar libros por idioma");
        System.out.println("4. Listar todos los autores");
        System.out.println("5. Buscar autores vivos en un año");
        System.out.println("6. Ver estadísticas por idioma");
        System.out.println("7. Top 10 libros más descargados");
        System.out.println("8. Estadísticas avanzadas");
        System.out.println("9. Buscar autor por nombre");
        System.out.println("10. Explorar autores");
        System.out.println("0. Salir");
        System.out.print("\nSeleccione una opción: ");
    }

    private void buscarLibroPorTitulo() throws Exception {
        System.out.print("\nIngrese el título del libro: ");
        String titulo = scanner.nextLine();

        Book book = bookService.searchBookByTitle(titulo);
        if (book != null) {
            System.out.println("\n=== [>] LIBRO ENCONTRADO ===");
            System.out.println("Título: " + book.getTitle());
            System.out.println("Autor: " + book.getAuthor());
            System.out.println("Idioma: " + book.getLanguage());
            System.out.println("Descargas: " + book.getDownloadCount());
        } else {
            System.out.println("\n[!] No se encontró ningún libro con ese título.");
        }
    }

    private void listarLibros() {
        List<Book> books = bookService.getAllBooks();

        if (books.isEmpty()) {
            System.out.println("\n[!] No hay libros en el catálogo.");
            return;
        }

        System.out.println("\n=== [*] CATÁLOGO DE LIBROS ===");
        books.forEach(book -> {
            System.out.println("\n[>] " + book.getTitle());
            System.out.println("   Autor: " + book.getAuthor());
            System.out.println("   Idioma: " + book.getLanguage());
            System.out.println("   Descargas: " + book.getDownloadCount());
        });
    }

    private void buscarLibrosPorIdioma() {
        System.out.print("\nIngrese el idioma (ej: en, es, fr): ");
        String idioma = scanner.nextLine().toLowerCase();

        List<Book> books = bookService.getBooksByLanguage(idioma);

        if (books.isEmpty()) {
            System.out.println("\n[!] No se encontraron libros en " + idioma);
            return;
        }

        System.out.println("\n=== [*] LIBROS EN " + idioma.toUpperCase() + " ===");
        books.forEach(book -> {
            System.out.println("\n[>] " + book.getTitle());
            System.out.println("   Autor: " + book.getAuthor());
            System.out.println("   Descargas: " + book.getDownloadCount());
        });
    }

    private void listarAutores() {
        List<Author> authors = bookService.getAllAuthors();

        if (authors.isEmpty()) {
            System.out.println("\n[!] No hay autores en el catálogo.");
            return;
        }

        System.out.println("\n=== [@] AUTORES REGISTRADOS ===");
        authors.forEach(author -> {
            System.out.println("\n[@] " + author.getName());
            if (author.getBirthYear() != null) {
                System.out.print("   Años: " + author.getBirthYear());
                if (author.getDeathYear() != null) {
                    System.out.print(" - " + author.getDeathYear());
                }
                System.out.println();
            }
        });
    }

    private void buscarAutoresVivosEnAnio() {
        System.out.print("\nIngrese el año a consultar: ");
        String input = scanner.nextLine().trim();

        try {
            // Validar que la entrada sea un número
            if (!input.matches("\\d{1,4}")) {
                throw new IllegalArgumentException("Por favor, ingrese un año válido (1-9999)");
            }

            int year = Integer.parseInt(input);
            List<Author> authorsAlive = bookService.getAuthorsAliveInYear(year);

            if (authorsAlive.isEmpty()) {
                System.out.println("\n[!] No se encontraron autores vivos en el año " + year);
                return;
            }

            System.out.println("\n=== [@] AUTORES VIVOS EN " + year + " ===");
            authorsAlive.forEach(author -> {
                System.out.println("\n[@] " + author.getName());
                System.out.print("   Años: " + author.getBirthYear());
                if (author.getDeathYear() != null) {
                    System.out.print(" - " + author.getDeathYear());
                } else {
                    System.out.print(" - presente");
                }
                System.out.println();

                // Calcular edad en el año consultado
                int edad = year - author.getBirthYear();
                if (author.getDeathYear() == null || year <= author.getDeathYear()) {
                    System.out.println("   Edad en " + year + ": " + edad + " años");
                }
            });
        } catch (IllegalArgumentException e) {
            System.out.println("\n[!] " + e.getMessage());
        } catch (Exception e) {
            System.out.println("\n[X] Error al buscar autores: " + e.getMessage());
        }
    }

    private void mostrarEstadisticas() {
        System.out.println("\n=== [#] ESTADÍSTICAS DE LIBROS POR IDIOMA ===");

        Map<String, Long> stats = statisticsService.getBookCountByLanguage();

        if (stats.isEmpty()) {
            System.out.println("\n[!] No hay libros registrados en la base de datos.");
            return;
        }

        // Mostrar estadísticas
        stats.forEach((lang, count) -> {
            String languageName = statisticsService.getLanguageName(lang);
            System.out.printf("\n[*] %s (%s): %d libro%s",
                languageName,
                lang.toUpperCase(),
                count,
                count == 1 ? "" : "s"
            );
        });

        // Mostrar total
        long total = stats.values().stream().mapToLong(Long::valueOf).sum();
        System.out.println("\n[*] Total de libros: " + total);

        // Mostrar porcentajes
        System.out.println("\n=== Distribución por idioma ===");
        stats.forEach((lang, count) -> {
            String languageName = statisticsService.getLanguageName(lang);
            double percentage = (count * 100.0) / total;
            System.out.printf("%s: %.1f%%\n", languageName, percentage);
        });
    }

    private void mostrarTop10() {
        System.out.println("\n=== [#] TOP 10 LIBROS MÁS DESCARGADOS ===");
        List<Book> topBooks = advancedStatsService.getTop10DownloadedBooks();

        if (topBooks.isEmpty()) {
            System.out.println("\n[!] No hay libros registrados.");
            return;
        }

        int rank = 1;
        for (Book book : topBooks) {
            System.out.printf("\n#%d [>] %s", rank++, book.getTitle());
            System.out.println("\n   Autor: " + book.getAuthor());
            System.out.println("   Descargas: " + book.getDownloadCount());
        }
    }

    private void mostrarEstadisticasAvanzadas() {
        System.out.println("\n=== [#] ESTADÍSTICAS AVANZADAS ===");
        Map<String, DoubleSummaryStatistics> stats =
            advancedStatsService.getDownloadStatisticsByLanguage();

        stats.forEach((lang, summary) -> {
            String languageName = statisticsService.getLanguageName(lang);
            System.out.printf("\n[*] %s (%s):", languageName, lang.toUpperCase());
            System.out.printf("\n   Promedio de descargas: %.2f", summary.getAverage());
            System.out.printf("\n   Máximo de descargas: %.0f", summary.getMax());
            System.out.printf("\n   Mínimo de descargas: %.0f", summary.getMin());
            System.out.printf("\n   Total de descargas: %.0f\n", summary.getSum());
        });
    }

    private void buscarAutorPorNombre() {
        System.out.print("\nIngrese el nombre del autor: ");
        String nombre = scanner.nextLine().trim();

        if (nombre.length() < 3) {
            System.out.println("\n[!] Por favor, ingrese al menos 3 caracteres.");
            return;
        }

        List<Author> authors = advancedStatsService.searchAuthorsByName(nombre);

        if (authors.isEmpty()) {
            System.out.println("\n[!] No se encontraron autores con ese nombre.");
            return;
        }

        System.out.println("\n=== [@] AUTORES ENCONTRADOS ===");
        authors.forEach(author -> {
            System.out.println("\n[@] " + author.getName());
            if (author.getBirthYear() != null) {
                System.out.print("   (" + author.getBirthYear());
                System.out.print(author.getDeathYear() != null ?
                    " - " + author.getDeathYear() : " - presente");
                System.out.println(")");
            }
        });
    }

    private void explorarAutores() {
        System.out.println("\n=== [?] EXPLORAR AUTORES ===");
        System.out.println("1. Autores vivos actualmente");
        System.out.println("2. Autores por siglo de nacimiento");
        System.out.println("0. Volver");

        try {
            int opcion = Integer.parseInt(scanner.nextLine());
            switch (opcion) {
                case 1 -> mostrarAutoresVivos();
                case 2 -> mostrarAutoresPorSiglo();
                case 0 -> { return; }
                default -> System.out.println("\n[!] Opción no válida.");
            }
        } catch (NumberFormatException e) {
            System.out.println("\n[!] Por favor, ingrese un número válido.");
        }
    }

    private void mostrarAutoresVivos() {
        List<Author> livingAuthors = advancedStatsService.getLivingAuthors();

        if (livingAuthors.isEmpty()) {
            System.out.println("\n[!] No se encontraron autores vivos.");
            return;
        }

        System.out.println("\n=== [@] AUTORES VIVOS ACTUALMENTE ===");
        livingAuthors.forEach(author -> {
            System.out.println("\n[@] " + author.getName() +
                             " (nacido en " + author.getBirthYear() + ")");
        });
    }

    private void mostrarAutoresPorSiglo() {
        System.out.print("\nIngrese el siglo (ej: 19 para siglo XIX): ");
        try {
            int siglo = Integer.parseInt(scanner.nextLine());
            List<Author> authors = advancedStatsService.getAuthorsBornInCentury(siglo);

            if (authors.isEmpty()) {
                System.out.println("\n[!] No se encontraron autores del siglo " + siglo);
                return;
            }

            System.out.println("\n=== [@] AUTORES DEL SIGLO " + siglo + " ===");
            authors.forEach(author -> {
                System.out.println("\n[@] " + author.getName() +
                                 " (" + author.getBirthYear() + "-" +
                                 (author.getDeathYear() != null ?
                                  author.getDeathYear() : "presente") + ")");
            });
        } catch (NumberFormatException e) {
            System.out.println("\n[!] Por favor, ingrese un número válido.");
        }
    }
}
