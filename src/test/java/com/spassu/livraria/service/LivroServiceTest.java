package com.spassu.livraria.service;

import com.spassu.livraria.dto.*;
import com.spassu.livraria.exception.*;
import com.spassu.livraria.model.*;
import com.spassu.livraria.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LivroServiceTest {

    @Mock private LivroRepository livroRepository;
    @Mock private AutorRepository autorRepository;
    @Mock private AssuntoRepository assuntoRepository;
    @Mock private LivroAutorRepository livroAutorRepository;
    @Mock private LivroAssuntoRepository livroAssuntoRepository;

    @InjectMocks private LivroService livroService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSalvar_DeveRetornarLivroRetornoDTO() {
        LivroDTO dto = new LivroDTO();
        dto.setTitulo("Livro Teste");
        dto.setEditora("Editora Teste");
        dto.setEdicao(1);
        dto.setAnoPublicacao("2023");
        dto.setValor(59.90);
        dto.setAutoresIds(List.of(1));
        dto.setAssuntosIds(List.of(10));

        Autor autor = new Autor();
        autor.setCodAu(1);
        autor.setNome("Autor Teste");

        Assunto assunto = new Assunto();
        assunto.setCodAs(10);
        assunto.setDescricao("Assunto Teste");

        Livro livroSalvo = new Livro();
        livroSalvo.setCodL(100);
        livroSalvo.setTitulo(dto.getTitulo());
        livroSalvo.setEditora(dto.getEditora());
        livroSalvo.setEdicao(dto.getEdicao());
        livroSalvo.setAnoPublicacao(dto.getAnoPublicacao());
        livroSalvo.setValor(5990);
        livroSalvo.setAutores(List.of(autor));
        livroSalvo.setAssuntos(List.of(assunto));

        when(autorRepository.findAllById(dto.getAutoresIds())).thenReturn(List.of(autor));
        when(assuntoRepository.findAllById(dto.getAssuntosIds())).thenReturn(List.of(assunto));
        when(livroRepository.save(any(Livro.class))).thenReturn(livroSalvo);

        LivroRetornoDTO retorno = livroService.salvar(dto);

        assertNotNull(retorno);
        assertEquals("Livro Teste", retorno.getTitulo());
        assertEquals(59.90, retorno.getValor());
        assertEquals("Autor Teste", retorno.getAutores().get(0).getNome());
        assertEquals("Assunto Teste", retorno.getAssuntos().get(0).getDescricao());
    }

    @Test
    void testListarTodos_DeveRetornarLista() {
        Livro livro = new Livro();
        livro.setCodL(1);
        livro.setTitulo("Testando");
        livro.setValor(2000);
        livro.setAutores(List.of());
        livro.setAssuntos(List.of());

        when(livroRepository.findAll()).thenReturn(List.of(livro));

        List<LivroRetornoDTO> livros = livroService.listarTodos();
        assertEquals(1, livros.size());
        assertEquals("Testando", livros.get(0).getTitulo());
    }

    @Test
    void testBuscarPorId_DeveRetornarDTO() {
        Livro livro = new Livro();
        livro.setCodL(1);
        livro.setTitulo("ID test");
        livro.setValor(1000);
        livro.setAutores(List.of());
        livro.setAssuntos(List.of());

        when(livroRepository.findById(1)).thenReturn(Optional.of(livro));

        LivroRetornoDTO dto = livroService.buscarPorId(1);
        assertEquals("ID test", dto.getTitulo());
        assertEquals(10.00, dto.getValor());
    }

    @Test
    void testExcluir_DeveExecutarComSucesso() {
        when(livroRepository.existsById(1)).thenReturn(true);

        livroService.excluir(1);

        verify(livroAutorRepository).deleteByLivroId(1);
        verify(livroAssuntoRepository).deleteByLivroId(1);
        verify(livroRepository).deleteById(1);
    }

    @Test
    void testBuscarPorTitulo_NaoEncontrado_DeveLancarExcecao() {
        when(livroRepository.findByTituloContainingIgnoreCase("xyz"))
                .thenReturn(Collections.emptyList());

        assertThrows(LivroNotFoundException.class, () -> livroService.buscarPorTitulo("xyz"));
    }

    @Test
    void testBuscarPorAutor_NaoEncontrado_DeveLancarExcecao() {
        when(autorRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(AutorNotFoundException.class, () -> livroService.buscarPorAutor(99));
    }

    @Test
    void testBuscarPorFaixaDeValor_ParametrosInvalidos_DeveLancarExcecao() {
        assertThrows(LivroParamValorException.class, () -> livroService.buscarPorFaixaDeValor(null, 1000));
        assertThrows(LivroParamValorException.class, () -> livroService.buscarPorFaixaDeValor(2000, 1000));
    }
}