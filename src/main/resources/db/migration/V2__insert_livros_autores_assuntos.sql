INSERT INTO Assunto (Descricao) VALUES ('Romance');
INSERT INTO Assunto (Descricao) VALUES ('Aventura');
INSERT INTO Assunto (Descricao) VALUES ('Drama');
INSERT INTO Assunto (Descricao) VALUES ('Ficção científica');
INSERT INTO Assunto (Descricao) VALUES ('Fantasia');
INSERT INTO Assunto (Descricao) VALUES ('Terror');
INSERT INTO Assunto (Descricao) VALUES ('Suspense');

INSERT INTO Autor (Nome) VALUES ('Jane Austen');
INSERT INTO Autor (Nome) VALUES ('Jules Verne');
INSERT INTO Autor (Nome) VALUES ('Victor Hugo');
INSERT INTO Autor (Nome) VALUES ('Isaac Asimov');
INSERT INTO Autor (Nome) VALUES ('J.R.R. Tolkien');
INSERT INTO Autor (Nome) VALUES ('Stephen King');
INSERT INTO Autor (Nome) VALUES ('Agatha Christie');
INSERT INTO Autor (Nome) VALUES ('Mary Shelley');
INSERT INTO Autor (Nome) VALUES ('Charlotte Bronte');
INSERT INTO Autor (Nome) VALUES ('George R.R. Martin');

INSERT INTO Livro (Titulo, Editora, Edicao, AnoPublicacao, Valor) VALUES
('Orgulho e Preconceito', 'Penguin', 1, '1813', 4500),
('Vinte Mil Léguas Submarinas', 'Abril', 2, '1870', 5900),
('Os Miseráveis', 'Martin Claret', 1, '1862', 6900),
('Fundação', 'Aleph', 3, '1951', 5500),
('O Senhor dos Anéis', 'HarperCollins', 1, '1954', 9900),
('It: A Coisa', 'Suma', 1, '1986', 7800),
('Assassinato no Expresso do Oriente', 'Globo', 4, '1934', 4200),
('Frankenstein', 'L&PM', 1, '1818', 3600),
('Jane Eyre', 'Penguin', 1, '1847', 4800),
('A Guerra dos Tronos', 'LeYa', 1, '1996', 8800);

INSERT INTO Livro_Autor (Livro_Codl, Autor_CodAu) VALUES (1, 1);
INSERT INTO Livro_Autor (Livro_Codl, Autor_CodAu) VALUES (2, 2);
INSERT INTO Livro_Autor (Livro_Codl, Autor_CodAu) VALUES (3, 3);
INSERT INTO Livro_Autor (Livro_Codl, Autor_CodAu) VALUES (4, 4);
INSERT INTO Livro_Autor (Livro_Codl, Autor_CodAu) VALUES (5, 5);
INSERT INTO Livro_Autor (Livro_Codl, Autor_CodAu) VALUES (6, 6);
INSERT INTO Livro_Autor (Livro_Codl, Autor_CodAu) VALUES (7, 7);
INSERT INTO Livro_Autor (Livro_Codl, Autor_CodAu) VALUES (8, 8);
INSERT INTO Livro_Autor (Livro_Codl, Autor_CodAu) VALUES (9, 9);
INSERT INTO Livro_Autor (Livro_Codl, Autor_CodAu) VALUES (10, 10);

INSERT INTO Livro_Assunto (Livro_Codl, Assunto_codAs) VALUES (1, 1);
INSERT INTO Livro_Assunto (Livro_Codl, Assunto_codAs) VALUES (2, 2);
INSERT INTO Livro_Assunto (Livro_Codl, Assunto_codAs) VALUES (3, 3);
INSERT INTO Livro_Assunto (Livro_Codl, Assunto_codAs) VALUES (4, 2);
INSERT INTO Livro_Assunto (Livro_Codl, Assunto_codAs) VALUES (4, 4);
INSERT INTO Livro_Assunto (Livro_Codl, Assunto_codAs) VALUES (5, 2);
INSERT INTO Livro_Assunto (Livro_Codl, Assunto_codAs) VALUES (5, 5);
INSERT INTO Livro_Assunto (Livro_Codl, Assunto_codAs) VALUES (6, 6);
INSERT INTO Livro_Assunto (Livro_Codl, Assunto_codAs) VALUES (6, 7);
INSERT INTO Livro_Assunto (Livro_Codl, Assunto_codAs) VALUES (7, 7);
INSERT INTO Livro_Assunto (Livro_Codl, Assunto_codAs) VALUES (8, 4);
INSERT INTO Livro_Assunto (Livro_Codl, Assunto_codAs) VALUES (9, 1);
INSERT INTO Livro_Assunto (Livro_Codl, Assunto_codAs) VALUES (10, 5);