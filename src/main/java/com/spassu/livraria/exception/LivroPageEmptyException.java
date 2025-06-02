package com.spassu.livraria.exception;

public class LivroPageEmptyException extends RuntimeException {

    public LivroPageEmptyException(Integer pagina) {
        super("A página " + pagina + " não possui nenhum Livro.");
    }
}
