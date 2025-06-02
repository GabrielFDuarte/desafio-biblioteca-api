package com.spassu.livraria.exception;

public class LivroParamValorException extends RuntimeException {

    public LivroParamValorException() {
        super("Valor mínimo e máximo para busca do Livro devem ser informados.");
    }

    public LivroParamValorException(Integer valorMin, Integer valorMax) {
        super("Valor mínimo (" + valorMin + ") não pode ser maior que o valor máximo (" + valorMax + ").");
    }
}
