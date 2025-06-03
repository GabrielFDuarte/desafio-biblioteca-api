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



## Documentação

A aplicação foi desenvolvida e documentada com apoio do **Swagger**. 

Para mais informações sobre os endpoints é possível acessar o seguinte endereço com a aplicação sendo executada:

```bash
http://localhost:8080/swagger-ui/index.html
```

Outros documentos relevantes incluem os **scripts** utilizados na base de dados **H2** e a collection de endpoints que pode ser importada via **Postman** para testes da API.

Os scripts da base de dados **H2** podem ser encontrados na pasta ```src/main/resources/db.migration``` e são os seguintes:
- V1__init.sql: Cria as tabelas iniciais de acordo com o modelo de dados;
- V2__insert_livros_autores_assuntos.sql: Insere dados nas tabelas para facilitar os testes;
- V3__criar_view_livros_autores_assuntos.sql: Cria a view responsável por buscar os dados do relatório;

Sobre a collection exportada do **Postman**, ela pode ser encontrada na pasta ```postman``` e possui as requisições organizadas de livros, autores, assuntos e relatório como descrito anteriormente na seção Funcionalidades Disponíveis. 

Testes podem ser feitos para todos os endpoints com o uso da collection.



## Observações

- O banco H2 é carregado em memória e reiniciado a cada start da aplicação.
- Os dados iniciais são inseridos automaticamente via scripts do Flyway.
- A aplicação possui testes unitários feitos com JUnit 5 e Mockito.
- A aplicação pode ser adaptada para uso com banco de dados persistente como PostgreSQL com poucos ajustes.
