package br.com.alura.challenge.literalura.model;

import com.fasterxml.jackson.annotation.JsonAlias;

public record AuthorData(String name,
                         @JsonAlias("birth_year") Integer birthYear,
                         @JsonAlias("death_year") Integer deathYear) {
}
