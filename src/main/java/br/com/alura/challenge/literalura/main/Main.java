package br.com.alura.challenge.literalura.main;

import br.com.alura.challenge.literalura.model.Author;
import br.com.alura.challenge.literalura.model.Book;
import br.com.alura.challenge.literalura.model.BookData;
import br.com.alura.challenge.literalura.model.SearchData;
import br.com.alura.challenge.literalura.repository.BookRepository;
import br.com.alura.challenge.literalura.service.DataConversor;
import br.com.alura.challenge.literalura.service.RequestApi;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.IntStream;

public class Main {
    private RequestApi requestApi = new RequestApi("https://gutendex.com/books");
    private DataConversor conversor = new DataConversor();
    private final Scanner scanner = new Scanner(System.in);
    private BookRepository repository;

    public Main(BookRepository repository) {
        this.repository = repository;
    }

    public void displayMenu() {
        var option = -1;

        while (option != 0) {
            System.out.println("""
                    ----------------------
                    Escolha uma opção:
                    1- buscar livro pelo título
                    2- listar livros registrados
                    3- listar autores registrados
                    4- listar autores vivos em um determinado ano
                    5- listar livros em um determinado idioma
                    
                    0- sair
                    """);

            option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1 -> searchBookMenu();
                case 2 -> displayBooks();
                case 3 -> displayAuthors();
                case 4 -> displayAliveAuthorsByYear();
                case 5 -> displayBooksByLang();
                case 0 -> System.out.println("\nSaindo...");
            }
        }
    }

    private void displayBooksByLang() {
        System.out.println("Digite a sigla do idioma para pesquisa: ");
        String lang = scanner.nextLine();

        List<Book> booksFound = repository.findAllBooksByLang(lang);

        if (booksFound.isEmpty()) {
            System.out.println("Não foram encontrados livros no idioma " + lang + "!");
        } else {
            System.out.println("LIVROS REGISTRADOS NESSE IDIOMA: \n");
            booksFound.forEach(System.out::println);
        }
    }

    private void displayAliveAuthorsByYear() {
        System.out.println("Digite o ano para a pesquisa dos autores vivos: ");
        var year = scanner.nextInt();
        scanner.nextLine();

        List<Author> authorsFound = repository.findAllAuthorsByYear(year);
        
        if (authorsFound.isEmpty()) {
            System.out.println("Não foram encontrados autores vivos em " + year + "!");
        } else {
            System.out.println("AUTORES REGISTRADOS VIVOS EM " + year + ":\n");
            authorsFound.forEach(System.out::println);
        }
    }

    private void displayAuthors() {
        List<Author> authorsFound = repository.findAllAuthors();

        if (authorsFound.isEmpty()) {
            System.out.println("Nenhum autor foi registrado até o momento!");
        } else {
            System.out.println("AUTORES REGISTRADOS: \n");
            authorsFound.forEach(System.out::println);
        }
        
    }

    private void displayBooks() {
        List<Book> booksFound = repository.findAll();
        System.out.println("LIVROS REGISTRADOS: \n");
        booksFound.forEach(System.out::println);
    }

    private void searchBookMenu() {
        System.out.println("Digite o nome do livro para pesquisa: ");
        String bookName = scanner.nextLine();

        String json = requestApi.getData(bookName);
        SearchData results = conversor.convertData(json, SearchData.class);
        BookData bookData = null;

        if (results.books().isEmpty()) {
            System.out.printf("Nenhum livro \"%s\" encontrado! Tente outro livro.%n", bookName);
        } else if (results.books().size() > 1) {
            bookData = selectSeveralBooks(results);
            System.out.println("Livro selecionado:\n" + bookData);
        } else {
            bookData = results.books().get(0);
            System.out.println(bookData);
        }

        if (bookData != null) {
            List<Book> books = new ArrayList<>();

            List<Author> authors = bookData.authors()
                    .stream()
                    .map(a -> new Author(a.name(), a.birthYear(), a.deathYear())).toList();

            books.add(new Book(bookData.title(), bookData.languages(), bookData.downloadCount()));
            authors.forEach(a -> a.setBooks(books));
            books.forEach(b -> b.setAuthors(authors));

            try {
                books.forEach(b -> repository.save(b));
            } catch (DataIntegrityViolationException e) {
                boolean bookIsPresent = repository.findByTitleContainingIgnoreCase(bookData.title()).isPresent();
                boolean allAuthorsIsPresent = authors.stream()
                        .map(a -> repository.findAllAuthorsByName(a.getName())).allMatch(Optional::isPresent);
                if (bookIsPresent && allAuthorsIsPresent) {
                    System.out.println("Este livro e autor já foram registrados!");
                } else {
                    List<Book> newBooks = handlerDataIntegrityViolationException(books, authors);
                    newBooks.forEach(b -> repository.saveAndFlush(b));
                }
            }
        }
    }

    private List<Book> handlerDataIntegrityViolationException(List<Book> books, List<Author> authors) {
        Optional<Book> bookOptional = repository.findByTitleContainingIgnoreCase(books.get(0).getTitle());
        List<Optional<Author>> authorsOptional = authors.stream()
                .map(a -> repository.findAllAuthorsByName(a.getName())).toList();

        List<Book> bookList = new ArrayList<>();
        if (bookOptional.isPresent()) {
            bookList.add(bookOptional.get());
        } else {
            bookList.addAll(books);
        }

        bookList.forEach(b -> b.setAuthors(IntStream.range(0, authorsOptional.size()).mapToObj(a -> {
            if (authorsOptional.get(a).isPresent()) {
                return authorsOptional.get(a).get();
            }
            return authors.get(a);
        }).toList()));

        return bookList;
    }

    private BookData selectSeveralBooks(SearchData results) { // selecting one of several books result
        System.out.println("Encontramos mais de um livro! Escolha um deles para registro: ");
        var option = -1;

        while (option < 1 || option > results.books().size()) {
            System.out.println(results);
            System.out.println("Escolha uma opção:");
            option = scanner.nextInt();
            scanner.nextLine();

            if (option < 1 || option > results.books().size()) {
                System.out.println("Opção incorreta! Tente novamente.");
            }
        }
        return results.books().get(option - 1);
    }
}
