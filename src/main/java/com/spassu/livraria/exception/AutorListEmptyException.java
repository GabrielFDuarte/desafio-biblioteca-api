package com.spassu.livraria.exception;

public class AutorListEmptyException extends RuntimeException {

    public AutorListEmptyException() {
        super("Não existe nenhum Autor cadastrado.");
    }
}
