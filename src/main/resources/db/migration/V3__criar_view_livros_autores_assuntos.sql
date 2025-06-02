CREATE OR REPLACE VIEW vw_livros_autores_assuntos AS
SELECT
    a.CodAu AS cod_autor,
    a.Nome AS autor_nome,
    l.CodL AS cod_livro,
    l.Titulo AS titulo,
    l.Editora AS editora,
    l.Edicao AS edicao,
    l.AnoPublicacao AS ano_publicacao,
    CONCAT('R$ ', CAST(ROUND(l.Valor / 100.0, 2) AS VARCHAR)) AS valor,
    GROUP_CONCAT(DISTINCT s.Descricao SEPARATOR ', ') AS assuntos
FROM Autor a
JOIN Livro_Autor la ON la.autor_codau = a.CodAu
JOIN Livro l ON l.CodL = la.livro_codl
LEFT JOIN Livro_Assunto ls ON ls.livro_codl = l.CodL
LEFT JOIN Assunto s ON s.codAS = ls.assunto_codas
GROUP BY a.CodAu, a.Nome, l.CodL, l.Titulo, l.Editora, l.Edicao, l.AnoPublicacao, l.Valor
ORDER BY a.Nome, l.Titulo;