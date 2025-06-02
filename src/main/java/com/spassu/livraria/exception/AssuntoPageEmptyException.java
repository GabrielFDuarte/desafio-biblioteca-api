package com.spassu.livraria.exception;

public class AssuntoPageEmptyException extends RuntimeException {

    public AssuntoPageEmptyException(Integer pagina) {
        super("A página " + pagina + " não possui nenhum Assunto.");
    }
}
