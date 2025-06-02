package com.spassu.livraria.dto;

import lombok.Data;

import java.util.List;

@Data
public class LivroRetornoDTO {

    private Integer codL;
    private String titulo;
    private String editora;
    private Integer edicao;
    private String anoPublicacao;
    private Double valor;
    private List<AutorRetornoDTO> autores;
    private List<AssuntoRetornoDTO> assuntos;

    public LivroRetornoDTO() {
    }

    public LivroRetornoDTO(LivroRetornoDTO livro) {
        this.codL = livro.getCodL();
        this.titulo = livro.getTitulo();
        this.editora = livro.getEditora();
        this.edicao = livro.getEdicao();
        this.anoPublicacao = livro.getAnoPublicacao();
        this.valor = livro.getValor();
        this.autores = livro.getAutores();
        this.assuntos = livro.getAssuntos();
    }
}
