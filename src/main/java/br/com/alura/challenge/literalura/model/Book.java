package br.com.alura.challenge.literalura.model;

import jakarta.persistence.*;

import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "livros")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "titulo", unique = true)
    private String title;
    @ElementCollection(fetch = FetchType.EAGER, targetClass = String.class)
    @Column(name = "idiomas")
    private List<String> languages;
    @Column(name = "numero_downloads")
    private Integer downloadCount;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "book_author",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id"))
    private List<Author> authors;

    public Book() {}

    public Book(String title, List<String> languages, Integer downloadCount) {
        this.title = title;
        this.languages = languages;
        this.downloadCount = downloadCount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getLanguages() {
        return languages;
    }

    public void setLanguages(List<String> languages) {
        this.languages = languages;
    }

    public Integer getDownloadCount() {
        return downloadCount;
    }

    public void setDownloadCount(Integer downloadCount) {
        this.downloadCount = downloadCount;
    }

    public List<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }

    @Override
    public String toString() {
        return """
                    ------ LIVRO ------
                    Titulo: %s
                    Autor/es: %s
                    Idioma/s: %s
                    Número de downloads: %s
                    -------------------
                    """.formatted(this.getTitle(),
                this.getAuthors().stream().map(Author::getName).collect(Collectors.joining(", ")),
                this.getLanguages(),
                this.getDownloadCount());
    }
}
