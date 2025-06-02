package com.spassu.livraria.exception;

public class AssuntoNotFoundException extends RuntimeException {

    public AssuntoNotFoundException(Integer id) {
        super("Assunto não encontrado para o seguinte id: " + id);
    }

    public AssuntoNotFoundException(String descricao) {
        super("Assunto não encontrado para a seguinte descrição: " + descricao);
    }
}
