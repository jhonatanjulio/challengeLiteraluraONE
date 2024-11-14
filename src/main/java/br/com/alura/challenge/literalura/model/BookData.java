package br.com.alura.challenge.literalura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
import java.util.stream.Collectors;

@JsonIgnoreProperties(ignoreUnknown = true)
public record BookData(String title,
                       List<AuthorData> authors,
                       List<String> languages,
                       @JsonAlias("download_count") Integer downloadCount) {
    @Override
    public String toString() {
        return """
                    ------ LIVRO ------
                    Titulo: %s
                    Autor/es: %s
                    Idioma/s: %s
                    Número de downloads: %s
                    -------------------
                    """.formatted(this.title(),
                this.authors().stream().map(AuthorData::name).collect(Collectors.joining(", ")),
                this.languages(),
                this.downloadCount());
    }
}
