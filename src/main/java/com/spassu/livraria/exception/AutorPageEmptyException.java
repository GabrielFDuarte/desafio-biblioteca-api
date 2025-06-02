package com.spassu.livraria.exception;

public class AutorPageEmptyException extends RuntimeException {

    public AutorPageEmptyException(Integer pagina) {
        super("A página " + pagina + " não possui nenhum Autor.");
    }
}
