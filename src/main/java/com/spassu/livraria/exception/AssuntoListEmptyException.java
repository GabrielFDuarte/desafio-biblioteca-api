package com.spassu.livraria.exception;

public class AssuntoListEmptyException extends RuntimeException {

    public AssuntoListEmptyException() {
        super("Não existe nenhum Assunto cadastrado.");
    }
}
