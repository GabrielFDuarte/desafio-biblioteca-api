package com.spassu.livraria.exception;

public class AutorNotFoundException extends RuntimeException {

    public AutorNotFoundException(Integer id) {
        super("Autor não encontrado para o seguinte id: " + id);
    }

    public AutorNotFoundException(String nome) {
        super("Autor não encontrado para o seguinte nome: " + nome);
    }
}
