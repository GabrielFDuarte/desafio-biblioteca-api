package com.spassu.livraria.controller;

import com.spassu.livraria.service.RelatorioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/relatorio")
@Tag(name = "Relatório", description = "Endpoint para gerenciamento do relatório.")
@RequiredArgsConstructor
public class RelatorioController {

    private final RelatorioService relatorioService;

    @GetMapping
    @Operation(summary = "Gera relatório.", description = "Gera um relatório PDF das informações de Livro, Autor e Assunto agrupadas por Autor.")
    public ResponseEntity<byte[]> gerarRelatorio() {
        byte[] pdf = relatorioService.gerarRelatorioLivrosPorAutor();
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=relatorio.pdf")
                .body(pdf);
    }
}
