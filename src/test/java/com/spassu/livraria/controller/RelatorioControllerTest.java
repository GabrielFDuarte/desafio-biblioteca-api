package com.spassu.livraria.controller;

import com.spassu.livraria.service.RelatorioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RelatorioControllerTest {

    @Mock
    private RelatorioService relatorioService;

    @InjectMocks
    private RelatorioController relatorioController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveGerarRelatorioPDFComSucesso() {
        byte[] pdfMock = "PDF TESTE".getBytes();
        when(relatorioService.gerarRelatorioLivrosPorAutor()).thenReturn(pdfMock);

        ResponseEntity<byte[]> response = relatorioController.gerarRelatorio();

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(MediaType.APPLICATION_PDF, response.getHeaders().getContentType());
        assertTrue(response.getHeaders().getFirst(HttpHeaders.CONTENT_DISPOSITION)
                .contains("attachment; filename=relatorio.pdf"));
        assertArrayEquals(pdfMock, response.getBody());

        verify(relatorioService, times(1)).gerarRelatorioLivrosPorAutor();
    }
}