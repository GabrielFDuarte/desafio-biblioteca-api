package com.spassu.livraria.exception;

import java.util.List;

public class AutorUniqueForBookException extends RuntimeException {

    public AutorUniqueForBookException(String titulo) {
        super("O livro '" + titulo + "' possui apenas um autor. Exclusão não permitida.");
    }

    public AutorUniqueForBookException(String titulo, List<Integer> autoresIds) {
        super("O livro '" + titulo + "' não pode ficar sem Autor. Exclusão não permitida.");
    }
}
