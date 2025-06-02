package com.spassu.livraria.dto;

import lombok.Data;

@Data
public class AutorRetornoDTO {

    private Integer codAu;
    private String nome;

    public AutorRetornoDTO() {
    }

    public AutorRetornoDTO(AutorRetornoDTO autor) {
        this.codAu = autor.getCodAu();
        this.nome = autor.getNome();
    }
}
