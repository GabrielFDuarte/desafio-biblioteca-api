package com.spassu.livraria.exception;

public class LivroListEmptyException extends RuntimeException {

    public LivroListEmptyException() {
        super("Não existe nenhum Livro cadastrado.");
    }
}
