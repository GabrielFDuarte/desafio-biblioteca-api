# Desafio Spassu - API Biblioteca

A aplicação consiste em uma **API RESTful** com frontend acoplado, simulando o funcionamento de uma **biblioteca virtual**. Ela permite o gerenciamento de **livros**, **autores** e **assuntos**, além de gerar um relatório em PDF.



## Tecnologias

- **Java 17**
- **Spring Boot 3.5.0**
  - Spring Web
  - Spring Data JPA
- **Flyway** para versionamento e execução de scripts no banco
- Banco de dados **H2**
- **HTML** e **Bootstrap**
- **JavaScript** 
- **JasperReports** para geração de PDF
- **Swagger**



## Funcionalidades Disponíveis

- **Livros**:
    - Cadastrar livro;
    - Listar todos os livros;
    - Listar de forma paginada todos os livros;
    - Buscar livro por id, título, autor e assunto;
    - Buscar livro por faixa de preço;
    - Apagar livro;
    - Editar livro;
    - Editar autor e assunto de livro;
- **Autores**:
    - Cadastrar autor;
    - Listar todos os autores;
    - Listar de forma paginada todos os autores;
    - Buscar autor por id e nome;
    - Apagar autor;
    - Editar autor;
- **Assuntos**:
    - Cadastrar assunto;
    - Listar todos os assuntos;
    - Listar de forma paginada todos os assuntos;
    - Buscar assunto por id e descrição;
    - Apagar assunto;
    - Editar assunto;
- **Relatório**:
    - Gerar relatório de livros, autores e assuntos agrupados por autor;

- Interface **Web**:
    - Implementa as funcionalidades descritas anteriormente.



## Execução do Projeto

### Pré-requisitos

- Java 17+ instalado
- Maven instalado (ou usar o wrapper incluído: `./mvnw`)

### Passos para execução

Após clonar o repositório, o projeto pode ser executado com o comando de execução spring-boot:

   ```bash
   ./mvnw spring-boot:run
   ```

Com o projeto sendo executado os endpoints ficam expostos e podem ser testados. 

A aplicação também pode ser acessada pelo navegador:
- **Frontend Web**: http://localhost:8080
- **Console do H2** para acesso à base de dados: http://localhost:8080/h2-console
    - JDBC URL: ```jdbc:h2:mem:livraria-db```
    - Usuário: ```spassu```
    - Senha: ```spassu```


