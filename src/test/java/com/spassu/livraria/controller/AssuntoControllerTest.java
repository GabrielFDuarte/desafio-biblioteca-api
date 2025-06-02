package com.spassu.livraria.controller;

import com.spassu.livraria.dto.AssuntoDTO;
import com.spassu.livraria.dto.AssuntoRetornoDTO;
import com.spassu.livraria.dto.PaginaDeAssuntosDTO;
import com.spassu.livraria.service.AssuntoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AssuntoControllerTest {

    private AssuntoService assuntoService;
    private AssuntoController controller;

    @BeforeEach
    void setup() {
        assuntoService = mock(AssuntoService.class);
        controller = new AssuntoController(assuntoService);
    }

    @Test
    void testCadastrar() {
        AssuntoDTO dto = new AssuntoDTO();
        AssuntoRetornoDTO retorno = new AssuntoRetornoDTO();
        retorno.setCodAs(1);

        when(assuntoService.salvar(dto)).thenReturn(retorno);

        ResponseEntity<AssuntoRetornoDTO> response = controller.cadastrar(dto);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals(URI.create("/api/assuntos/1"), response.getHeaders().getLocation());
        assertEquals(retorno, response.getBody());
        verify(assuntoService).salvar(dto);
    }

    @Test
    void testListarTodosComPaginacao() {
        int pagina = 0, tamanho = 10;
        Page<AssuntoRetornoDTO> paginacao = new PageImpl<>(List.of(new AssuntoRetornoDTO()));

        when(assuntoService.listarPaginado(pagina, tamanho)).thenReturn(paginacao);

        ResponseEntity<?> response = controller.listarTodos(pagina, tamanho);

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody() instanceof PaginaDeAssuntosDTO);
        verify(assuntoService).listarPaginado(pagina, tamanho);
    }

    @Test
    void testListarTodosSemPaginacao() {
        List<AssuntoRetornoDTO> lista = Arrays.asList(new AssuntoRetornoDTO(), new AssuntoRetornoDTO());

        when(assuntoService.listarTodos()).thenReturn(lista);

        ResponseEntity<?> response = controller.listarTodos(null, null);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(lista, response.getBody());
        verify(assuntoService).listarTodos();
    }

    @Test
    void testBuscarPorId() {
        int id = 1;
        AssuntoRetornoDTO retorno = new AssuntoRetornoDTO();

        when(assuntoService.buscarPorId(id)).thenReturn(retorno);

        ResponseEntity<AssuntoRetornoDTO> response = controller.buscarPorId(id);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(retorno, response.getBody());
        verify(assuntoService).buscarPorId(id);
    }

    @Test
    void testBuscarPorDescricao() {
        String descricao = "história";
        List<AssuntoRetornoDTO> lista = Arrays.asList(new AssuntoRetornoDTO());

        when(assuntoService.buscarPorDescricao(descricao)).thenReturn(lista);

        ResponseEntity<List<AssuntoRetornoDTO>> response = controller.buscarPorDescricao(descricao);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(lista, response.getBody());
        verify(assuntoService).buscarPorDescricao(descricao);
    }

    @Test
    void testExcluir() {
        int id = 1;

        doNothing().when(assuntoService).excluir(id);

        ResponseEntity<Void> response = controller.excluir(id);

        assertEquals(204, response.getStatusCodeValue());
        assertNull(response.getBody());
        verify(assuntoService).excluir(id);
    }

    @Test
    void testAtualizar() {
        int id = 1;
        AssuntoDTO dto = new AssuntoDTO();
        AssuntoRetornoDTO retorno = new AssuntoRetornoDTO();

        when(assuntoService.atualizar(id, dto)).thenReturn(retorno);

        ResponseEntity<AssuntoRetornoDTO> response = controller.atualizar(id, dto);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(retorno, response.getBody());
        verify(assuntoService).atualizar(id, dto);
    }
}
