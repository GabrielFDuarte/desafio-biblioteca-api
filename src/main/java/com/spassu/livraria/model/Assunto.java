package com.spassu.livraria.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

@Entity
@Table(name = "ASSUNTO")
@Data
@AllArgsConstructor
public class Assunto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CODAS")
    private Integer codAs;

    @Column(name = "DESCRICAO")
    private String descricao;

    public Assunto() {
    }

    public Assunto(Assunto assunto) {
        this.codAs = assunto.getCodAs();
        this.descricao = assunto.getDescricao();
    }
}
