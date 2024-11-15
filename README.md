# :books: Challenge LiterAlura ![MIT License](https://img.shields.io/badge/License-MIT-green.svg)
Challenge Conversor de Moedas implementado com a [API Gutendex](https://gutendex.com).<br>
Proposto pela Oracle Next Education, na formação de Back-end Java, em parceria com a Alura.

## :wrench: Sobre o projeto
- Uma aplicação back-end simples e robusta, estruturada utilizando as melhores práticas de programação, arquitetada em layers, a qual separa o código por funcionalidades.
- O objetivo da aplicação é buscar livros através do consumo da API Gutendex, realizar a persistência dos dados dos livros em um banco de dados relacional, e oferecer funcionalidades para a pesquisa dos registros já persistidos.
- A interação com a aplicação é realizada através de linhas de comando, com o auxílio de um menu de opções. 

## :clipboard: Como funciona?
- Ao iniciar a aplicação, o menu de interação é exibido, permitindo que o usuário escolha uma das opções, entre 1 e 5, sendo o 0 para finalizar a aplicação (o input do usuário deve ser um número inteiro):
<br>
<div align="center">
  <img alt="Menu Inicial" src="./img-readme/menu-inicial.png">
</div>

- Ao escolher a opção "1- buscar livro pelo título", o usuário deverá digitar o nome de um livro ou de um autor para busca, o resultado será exibido e persistido no banco de dados:
<br>
<div align="center">
  <img alt="Busca por titulo" src="./img-readme/busca-titulo.png">
</div>

- Com livros e autores já persistidos no banco de dados, o usuário pode optar pelas opções entre 2 e 5, as quais são responsáveis por fazer buscas personalizadas dos registros no banco:
<br>
<div align="center">
  <img alt="Busca livros registrados" src="./img-readme/busca-livros-registrados.png">
</div>
<br>
<div align="center">
  <img alt="Busca autores registrados" src="./img-readme/busca-autores-registrados.png">
</div>
<br>

## :computer: Tecnologias e ferramentas utilizadas
- Java (JDK 17)
- Spring Boot e JPA Hibernate
- IntelliJ e Git
- [Gutendex API](https://gutendex.com)
- [PostgreSQL](https://www.postgresql.org)