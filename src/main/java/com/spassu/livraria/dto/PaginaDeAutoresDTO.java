package com.spassu.livraria.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaginaDeAutoresDTO {

    private List<AutorRetornoDTO> conteudo;
    private int paginaAtual;
    private int tamanhoPagina;
    private long totalDeElementos;
    private int totalDePaginas;
    private boolean primeiraPagina;
    private boolean ultimaPagina;

    public PaginaDeAutoresDTO(Page<AutorRetornoDTO> pagina) {
        this.conteudo = pagina.getContent().stream().map(AutorRetornoDTO::new).collect(Collectors.toList());
        this.paginaAtual = pagina.getNumber();
        this.tamanhoPagina = pagina.getSize();
        this.totalDeElementos = pagina.getTotalElements();
        this.totalDePaginas = pagina.getTotalPages();
        this.primeiraPagina = pagina.isFirst();
        this.ultimaPagina = pagina.isLast();
    }
}
