package com.spassu.livraria.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "LIVRO")
@Data
@AllArgsConstructor
public class Livro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CODL")
    private Integer codL;

    @Column(name = "TITULO")
    private String titulo;

    @Column(name = "EDITORA")
    private String editora;

    @Column(name = "EDICAO")
    private Integer edicao;

    @Column(name = "ANOPUBLICACAO")
    private String anoPublicacao;

    @Column(name = "VALOR")
    private Integer valor;

    @ManyToMany
    @JoinTable(name = "livro_autor",
        joinColumns = @JoinColumn(name = "livro_codl"),
        inverseJoinColumns = @JoinColumn(name = "autor_codau"))
    private List<Autor> autores;

    @ManyToMany
    @JoinTable(name = "livro_assunto",
        joinColumns = @JoinColumn(name = "livro_codl"),
        inverseJoinColumns = @JoinColumn(name = "assunto_codas"))
    private List<Assunto> assuntos;

    public Livro() {
    }

    public Livro(Livro livro) {
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
