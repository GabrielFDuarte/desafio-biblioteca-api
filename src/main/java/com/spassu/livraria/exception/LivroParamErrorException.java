package com.spassu.livraria.exception;

import java.util.List;

public class LivroParamErrorException extends RuntimeException {

    public LivroParamErrorException() {
        super("Um ou mais IDs de Autores/Assuntos não foram encontrados.");
    }

    public LivroParamErrorException(List<Integer> lista) {
        super("O Livro deve possuir ao menos um Autor/Assunto.");
    }

    public LivroParamErrorException(String anoPublicacao) {
        super("Ano de publicação do Livro é inválido: " + anoPublicacao);
    }

    public LivroParamErrorException(String anoPublicacao, Integer anoAtual) {
        super("Ano de publicação do Livro (" + anoPublicacao + ") é superior ao ano atual (" + anoAtual + ").");
    }
}
