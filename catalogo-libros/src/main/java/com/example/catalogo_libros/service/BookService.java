package com.ejemplo.catalogo_libros.service;

import com.ejemplo.catalogo_libros.model.Book;
import com.ejemplo.catalogo_libros.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Optional;
import java.util.List;

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;

    private static final String GUTENDEX_API_URL = "https://gutendex.com/books?search=";

    public String searchAndRegisterBook(String title) {
        // Verificar si el libro ya está en la base de datos
        Optional<Book> existingBook = bookRepository.findByTitle(title);
        if (existingBook.isPresent()) {
            return "El libro ya está registrado en la base de datos.";
        }

        // Buscar el libro en la API de Gutendex
        RestTemplate restTemplate = new RestTemplate();
        String url = GUTENDEX_API_URL + title;
        GutendexResponse response = restTemplate.getForObject(url, GutendexResponse.class);

        // Verificar si se encontró el libro
        if (response == null || response.getResults().isEmpty()) {
            return "El libro no fue encontrado.";
        }

        // Registrar el libro en la base de datos
        GutendexBook apiBook = response.getResults().get(0);
        Book book = new Book();
        book.setTitle(apiBook.getTitle());
        book.setAuthor(apiBook.getAuthors().get(0).getName());
        book.setLanguage(apiBook.getLanguages().get(0));
        book.setDownloads(apiBook.getDownload_count());

        bookRepository.save(book);

        return "Libro registrado con éxito.";
    }
}

// Clases para manejar la respuesta de la API
class GutendexResponse {
    private List<GutendexBook> results;

    // Getters y Setters
    public List<GutendexBook> getResults() {
        return results;
    }

    public void setResults(List<GutendexBook> results) {
        this.results = results;
    }
}

class GutendexBook {
    private String title;
    private List<GutendexAuthor> authors;
    private List<String> languages;
    private int download_count;

    // Getters y Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<GutendexAuthor> getAuthors() {
        return authors;
    }

    public void setAuthors(List<GutendexAuthor> authors) {
        this.authors = authors;
    }

    public List<String> getLanguages() {
        return languages;
    }

    public void setLanguages(List<String> languages) {
        this.languages = languages;
    }

    public int getDownload_count() {
        return download_count;
    }

    public void setDownload_count(int download_count) {
        this.download_count = download_count;
    }
}

class GutendexAuthor {
    private String name;

    // Getters y Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
