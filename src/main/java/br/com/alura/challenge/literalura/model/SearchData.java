package br.com.alura.challenge.literalura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SearchData(@JsonAlias("results") List<BookData> books) {
    @Override
    public String toString() {
        StringBuilder booksListString = new StringBuilder();

        for (var i = 0; i < books().size(); i++) {
            booksListString.append("\n").append(i+1).append("-\n").append(books().get(i).toString());
        }

        return booksListString.toString();
    }
}
