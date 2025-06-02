package com.spassu.livraria.exception;

public class RelatorioErroException extends RuntimeException {

    public RelatorioErroException() {
        super("Erro interno durante geração de relatório.");
    }
}
