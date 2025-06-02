package com.spassu.livraria.service;

import com.spassu.livraria.dto.AssuntoDTO;
import com.spassu.livraria.dto.AssuntoRetornoDTO;
import com.spassu.livraria.exception.*;
import com.spassu.livraria.model.Assunto;
import com.spassu.livraria.model.Livro;
import com.spassu.livraria.repository.AssuntoRepository;
import com.spassu.livraria.repository.LivroAssuntoRepository;
import com.spassu.livraria.repository.LivroRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AssuntoServiceTest {

    private AssuntoRepository assuntoRepository;
    private LivroRepository livroRepository;
    private LivroAssuntoRepository livroAssuntoRepository;
    private AssuntoService assuntoService;

    @BeforeEach
    void setUp() {
        assuntoRepository = mock(AssuntoRepository.class);
        livroRepository = mock(LivroRepository.class);
        livroAssuntoRepository = mock(LivroAssuntoRepository.class);
        assuntoService = new AssuntoService(assuntoRepository, livroRepository, livroAssuntoRepository);
    }

    @Test
    void salvar_DeveRetornarAssuntoRetornoDTO() {
        AssuntoDTO dto = new AssuntoDTO();
        dto.setDescricao("Tecnologia");

        Assunto assunto = new Assunto();
        assunto.setCodAs(1);
        assunto.setDescricao("Tecnologia");

        when(assuntoRepository.save(any(Assunto.class))).thenReturn(assunto);

        AssuntoRetornoDTO retorno = assuntoService.salvar(dto);

        assertEquals(1, retorno.getCodAs());
        assertEquals("Tecnologia", retorno.getDescricao());
    }

    @Test
    void listarTodos_DeveRetornarListaDeAssuntos() {
        Assunto assunto = new Assunto();
        assunto.setCodAs(1);
        assunto.setDescricao("Educação");

        when(assuntoRepository.findAll()).thenReturn(List.of(assunto));

        List<AssuntoRetornoDTO> lista = assuntoService.listarTodos();

        assertEquals(1, lista.size());
        assertEquals("Educação", lista.get(0).getDescricao());
    }

    @Test
    void listarTodos_DeveLancarExcecao_SeListaVazia() {
        when(assuntoRepository.findAll()).thenReturn(List.of());

        assertThrows(AssuntoListEmptyException.class, () -> assuntoService.listarTodos());
    }

    @Test
    void listarPaginado_DeveRetornarPagina() {
        Assunto assunto = new Assunto();
        assunto.setCodAs(1);
        assunto.setDescricao("Saúde");

        Page<Assunto> pagina = new PageImpl<>(List.of(assunto));

        when(assuntoRepository.findAll(any(Pageable.class))).thenReturn(pagina);

        Page<AssuntoRetornoDTO> resultado = assuntoService.listarPaginado(0, 10);

        assertEquals(1, resultado.getContent().size());
        assertEquals("Saúde", resultado.getContent().get(0).getDescricao());
    }

    @Test
    void listarPaginado_DeveLancarAssuntoListEmptyException() {
        Page<Assunto> paginaVazia = new PageImpl<>(List.of(), PageRequest.of(0, 10), 0);
        when(assuntoRepository.findAll(any(Pageable.class))).thenReturn(paginaVazia);

        assertThrows(AssuntoListEmptyException.class, () -> assuntoService.listarPaginado(0, 10));
    }

    @Test
    void listarPaginado_DeveLancarAssuntoPageEmptyException() {
        Page<Assunto> paginaSemConteudo = new PageImpl<>(List.of(), PageRequest.of(5, 10), 1);
        when(assuntoRepository.findAll(any(Pageable.class))).thenReturn(paginaSemConteudo);

        assertThrows(AssuntoPageEmptyException.class, () -> assuntoService.listarPaginado(5, 10));
    }

    @Test
    void buscarPorId_DeveRetornarAssunto() {
        Assunto assunto = new Assunto();
        assunto.setCodAs(1);
        assunto.setDescricao("Ciência");

        when(assuntoRepository.findById(1)).thenReturn(Optional.of(assunto));

        AssuntoRetornoDTO retorno = assuntoService.buscarPorId(1);

        assertEquals("Ciência", retorno.getDescricao());
    }

    @Test
    void buscarPorId_DeveLancarAssuntoNotFoundException() {
        when(assuntoRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(AssuntoNotFoundException.class, () -> assuntoService.buscarPorId(1));
    }

    @Test
    void excluir_DeveExcluirAssuntoComSucesso() {
        Assunto assunto = new Assunto();
        assunto.setCodAs(1);
        assunto.setDescricao("Filosofia");

        when(assuntoRepository.findById(1)).thenReturn(Optional.of(assunto));
        when(livroRepository.findByAssuntoId(1)).thenReturn(List.of());
        doNothing().when(livroAssuntoRepository).deleteByAssuntoId(1);
        doNothing().when(assuntoRepository).deleteById(1);

        assertDoesNotThrow(() -> assuntoService.excluir(1));
    }

    @Test
    void excluir_DeveLancarAssuntoUniqueForBookException() {
        Livro livro = new Livro();
        livro.setCodL(10);
        livro.setTitulo("Livro Único");

        Assunto assunto = new Assunto();
        assunto.setCodAs(1);

        when(assuntoRepository.findById(1)).thenReturn(Optional.of(assunto));
        when(livroRepository.findByAssuntoId(1)).thenReturn(List.of(livro));
        when(livroAssuntoRepository.countByLivroCodL(10)).thenReturn(1);

        assertThrows(AssuntoUniqueForBookException.class, () -> assuntoService.excluir(1));
    }

    @Test
    void atualizar_DeveAtualizarDescricao() {
        Assunto assunto = new Assunto();
        assunto.setCodAs(1);
        assunto.setDescricao("Antiga");

        AssuntoDTO dto = new AssuntoDTO();
        dto.setDescricao("Nova");

        when(assuntoRepository.findById(1)).thenReturn(Optional.of(assunto));
        when(assuntoRepository.save(any(Assunto.class))).thenReturn(assunto);

        AssuntoRetornoDTO retorno = assuntoService.atualizar(1, dto);

        assertEquals("Nova", retorno.getDescricao());
    }

    @Test
    void buscarPorDescricao_DeveRetornarLista() {
        Assunto assunto = new Assunto();
        assunto.setCodAs(1);
        assunto.setDescricao("História");

        when(assuntoRepository.findByDescricaoContainingIgnoreCase("Hist")).thenReturn(List.of(assunto));

        List<AssuntoRetornoDTO> retorno = assuntoService.buscarPorDescricao("Hist");

        assertEquals(1, retorno.size());
        assertEquals("História", retorno.get(0).getDescricao());
    }

    @Test
    void buscarPorDescricao_DeveLancarAssuntoNotFoundException() {
        when(assuntoRepository.findByDescricaoContainingIgnoreCase("XYZ")).thenReturn(List.of());

        assertThrows(AssuntoNotFoundException.class, () -> assuntoService.buscarPorDescricao("XYZ"));
    }
}