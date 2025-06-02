package com.spassu.livraria.exception;

public class RelatorioDatabaseException extends RuntimeException {

    public RelatorioDatabaseException() {
        super("Erro de conexão no banco H2.");
    }
}
