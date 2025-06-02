package com.spassu.livraria.exception;

public class LivroNotFoundException extends RuntimeException {

    public LivroNotFoundException(Integer id) {
        super("Livro não encontrado para o seguinte id: " + id);
    }

    public LivroNotFoundException(String nome) {
        super("Livro não encontrado para o seguinte título/autor/assunto: " + nome);
    }

    public LivroNotFoundException(Integer valorMin, Integer valorMax) {
        super("Nenhum Livro encontrado na faixa de valor entre " + valorMin + " e " + valorMax);
    }
}
