package com.spassu.livraria.exception;

import java.util.List;

public class AssuntoUniqueForBookException extends RuntimeException {

    public AssuntoUniqueForBookException(String titulo) {
        super("O livro '" + titulo + "' possui apenas um assunto. Exclusão não permitida.");
    }

    public AssuntoUniqueForBookException(String titulo, List<Integer> assuntosIds) {
        super("O livro '" + titulo + "' não pode ficar sem Assunto. Exclusão não permitida.");
    }
}
