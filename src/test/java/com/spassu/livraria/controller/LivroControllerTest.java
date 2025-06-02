package com.spassu.livraria.controller;

import com.spassu.livraria.dto.LivroDTO;
import com.spassu.livraria.dto.LivroRetornoDTO;
import com.spassu.livraria.dto.PaginaDeLivrosDTO;
import com.spassu.livraria.service.LivroService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LivroControllerTest {

    @InjectMocks
    private LivroController livroController;

    @Mock
    private LivroService livroService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCadastrar() {
        LivroDTO dto = new LivroDTO();
        LivroRetornoDTO retorno = new LivroRetornoDTO();
        when(livroService.salvar(dto)).thenReturn(retorno);

        ResponseEntity<LivroRetornoDTO> response = livroController.cadastrar(dto);

        assertEquals(retorno, response.getBody());
        verify(livroService).salvar(dto);
    }

    @Test
    void testListarTodosComPaginacao() {
        Page<LivroRetornoDTO> page = new PageImpl<>(List.of(new LivroRetornoDTO()));
        when(livroService.listarPaginado(0, 10)).thenReturn(page);

        ResponseEntity<?> response = livroController.listarTodos(0, 10);

        assertTrue(response.getBody() instanceof PaginaDeLivrosDTO);
        verify(livroService).listarPaginado(0, 10);
    }

    @Test
    void testListarTodosSemPaginacao() {
        List<LivroRetornoDTO> lista = List.of(new LivroRetornoDTO());
        when(livroService.listarTodos()).thenReturn(lista);

        ResponseEntity<?> response = livroController.listarTodos(null, null);

        assertEquals(lista, response.getBody());
        verify(livroService).listarTodos();
    }

    @Test
    void testBuscarPorId() {
        LivroRetornoDTO livro = new LivroRetornoDTO();
        when(livroService.buscarPorId(1)).thenReturn(livro);

        ResponseEntity<LivroRetornoDTO> response = livroController.buscarPorId(1);

        assertEquals(livro, response.getBody());
        verify(livroService).buscarPorId(1);
    }

    @Test
    void testBuscarPorTitulo() {
        List<LivroRetornoDTO> livros = List.of(new LivroRetornoDTO());
        when(livroService.buscarPorTitulo("Titulo")).thenReturn(livros);

        ResponseEntity<List<LivroRetornoDTO>> response = livroController.buscarPorTitulo("Titulo");

        assertEquals(livros, response.getBody());
        verify(livroService).buscarPorTitulo("Titulo");
    }

    @Test
    void testBuscarPorAutor() {
        List<LivroRetornoDTO> livros = List.of(new LivroRetornoDTO());
        when(livroService.buscarPorAutor(1)).thenReturn(livros);

        List<LivroRetornoDTO> result = livroController.buscarPorAutor(1);

        assertEquals(livros, result);
        verify(livroService).buscarPorAutor(1);
    }

    @Test
    void testBuscarPorAssunto() {
        List<LivroRetornoDTO> livros = List.of(new LivroRetornoDTO());
        when(livroService.buscarPorAssunto(1)).thenReturn(livros);

        List<LivroRetornoDTO> result = livroController.buscarPorAssunto(1);

        assertEquals(livros, result);
        verify(livroService).buscarPorAssunto(1);
    }

    @Test
    void testBuscarPorFaixaDeValor() {
        List<LivroRetornoDTO> livros = List.of(new LivroRetornoDTO());
        when(livroService.buscarPorFaixaDeValor(10, 50)).thenReturn(livros);

        List<LivroRetornoDTO> result = livroController.buscarPorFaixaDeValor(10, 50);

        assertEquals(livros, result);
        verify(livroService).buscarPorFaixaDeValor(10, 50);
    }

    @Test
    void testExcluir() {
        ResponseEntity<Void> response = livroController.excluir(1);

        assertEquals(204, response.getStatusCodeValue());
        verify(livroService).excluir(1);
    }

    @Test
    void testAtualizar() {
        LivroDTO dto = new LivroDTO();
        LivroRetornoDTO retorno = new LivroRetornoDTO();
        when(livroService.atualizar(1, dto)).thenReturn(retorno);

        ResponseEntity<LivroRetornoDTO> response = livroController.atualizar(1, dto);

        assertEquals(retorno, response.getBody());
        verify(livroService).atualizar(1, dto);
    }

    @Test
    void testVincularDesvincularAutores() {
        List<Integer> autoresIds = Arrays.asList(1, 2);
        LivroRetornoDTO retorno = new LivroRetornoDTO();
        when(livroService.vincularDesvincularAutores(1, autoresIds)).thenReturn(retorno);

        ResponseEntity<LivroRetornoDTO> response = livroController.vincularDesvincularAutores(1, autoresIds);

        assertEquals(retorno, response.getBody());
        verify(livroService).vincularDesvincularAutores(1, autoresIds);
    }

    @Test
    void testVincularDesvincularAssuntos() {
        List<Integer> assuntosIds = Arrays.asList(1, 2);
        LivroRetornoDTO retorno = new LivroRetornoDTO();
        when(livroService.vincularDesvincularAssuntos(1, assuntosIds)).thenReturn(retorno);

        ResponseEntity<LivroRetornoDTO> response = livroController.vincularDesvincularAssuntos(1, assuntosIds);

        assertEquals(retorno, response.getBody());
        verify(livroService).vincularDesvincularAssuntos(1, assuntosIds);
    }
}