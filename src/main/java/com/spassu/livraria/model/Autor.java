package com.spassu.livraria.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

@Entity
@Table(name = "AUTOR")
@Data
@AllArgsConstructor
public class Autor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CODAU")
    private Integer codAu;

    @Column(name = "NOME")
    private String nome;

    public Autor() {
    }

    public Autor(Autor autor) {
        this.codAu = autor.getCodAu();
        this.nome = autor.getNome();
    }
}
