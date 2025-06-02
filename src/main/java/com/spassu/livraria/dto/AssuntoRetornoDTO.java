package com.spassu.livraria.dto;

import lombok.Data;

@Data
public class AssuntoRetornoDTO {

    private Integer codAs;
    private String descricao;

    public AssuntoRetornoDTO() {
    }

    public AssuntoRetornoDTO(AssuntoRetornoDTO assunto) {
        this.codAs = assunto.getCodAs();
        this.descricao = assunto.getDescricao();
    }
}
