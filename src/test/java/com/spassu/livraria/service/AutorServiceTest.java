package com.spassu.livraria.service;

import com.spassu.livraria.dto.AutorDTO;
import com.spassu.livraria.dto.AutorRetornoDTO;
import com.spassu.livraria.exception.AutorListEmptyException;
import com.spassu.livraria.exception.AutorNotFoundException;
import com.spassu.livraria.exception.AutorPageEmptyException;
import com.spassu.livraria.exception.AutorUniqueForBookException;
import com.spassu.livraria.model.Autor;
import com.spassu.livraria.model.Livro;
import com.spassu.livraria.repository.AutorRepository;
import com.spassu.livraria.repository.LivroAutorRepository;
import com.spassu.livraria.repository.LivroRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AutorServiceTest {

    @InjectMocks
    private AutorService autorService;

    @Mock
    private AutorRepository autorRepository;

    @Mock
    private LivroRepository livroRepository;

    @Mock
    private LivroAutorRepository livroAutorRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void salvar_deveSalvarAutorERetornarDTO() {
        AutorDTO dto = new AutorDTO("João");
        Autor autorSalvo = new Autor();
        autorSalvo.setCodAu(1);
        autorSalvo.setNome("João");

        when(autorRepository.save(any(Autor.class))).thenReturn(autorSalvo);

        AutorRetornoDTO resultado = autorService.salvar(dto);

        assertEquals("João", resultado.getNome());
        assertEquals(1, resultado.getCodAu());
    }

    @Test
    void listarTodos_deveRetornarListaDeAutores() {
        Autor autor = new Autor();
        autor.setCodAu(1);
        autor.setNome("João");

        when(autorRepository.findAll()).thenReturn(List.of(autor));

        List<AutorRetornoDTO> lista = autorService.listarTodos();

        assertEquals(1, lista.size());
        assertEquals("João", lista.get(0).getNome());
    }

    @Test
    void listarTodos_deveLancarExcecao_QuandoListaVazia() {
        when(autorRepository.findAll()).thenReturn(Collections.emptyList());

        assertThrows(AutorListEmptyException.class, () -> autorService.listarTodos());
    }

    @Test
    void listarPaginado_deveRetornarPaginaDeAutores() {
        Autor autor = new Autor();
        autor.setCodAu(1);
        autor.setNome("João");

        Page<Autor> pagina = new PageImpl<>(List.of(autor), PageRequest.of(0, 10), 1);

        when(autorRepository.findAll(any(Pageable.class))).thenReturn(pagina);

        Page<AutorRetornoDTO> resultado = autorService.listarPaginado(0, 10);

        assertEquals(1, resultado.getTotalElements());
        assertEquals("João", resultado.getContent().get(0).getNome());
    }

    @Test
    void listarPaginado_deveLancarAutorListEmptyException() {
        Page<Autor> paginaVazia = new PageImpl<>(Collections.emptyList(), PageRequest.of(0, 10), 0);

        when(autorRepository.findAll(any(Pageable.class))).thenReturn(paginaVazia);

        assertThrows(AutorListEmptyException.class, () -> autorService.listarPaginado(0, 10));
    }

    @Test
    void listarPaginado_deveLancarAutorPageEmptyException() {
        Page<Autor> paginaSemConteudo = new PageImpl<>(Collections.emptyList(), PageRequest.of(1, 10), 1);

        when(autorRepository.findAll(any(Pageable.class))).thenReturn(paginaSemConteudo);

        assertThrows(AutorPageEmptyException.class, () -> autorService.listarPaginado(1, 10));
    }

    @Test
    void buscarPorId_deveRetornarAutor() {
        Autor autor = new Autor();
        autor.setCodAu(1);
        autor.setNome("João");

        when(autorRepository.findById(1)).thenReturn(Optional.of(autor));

        AutorRetornoDTO dto = autorService.buscarPorId(1);

        assertEquals("João", dto.getNome());
    }

    @Test
    void buscarPorId_deveLancarExcecao_SeNaoEncontrado() {
        when(autorRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(AutorNotFoundException.class, () -> autorService.buscarPorId(99));
    }

    @Test
    void excluir_deveExcluirAutor() {
        int autorId = 1;
        Autor autor = new Autor();
        autor.setCodAu(autorId);
        autor.setNome("João");

        Livro livro = new Livro();
        livro.setCodL(1);
        livro.setTitulo("Livro 1");

        when(autorRepository.findById(autorId)).thenReturn(Optional.of(autor));
        when(livroRepository.findByAutorId(autorId)).thenReturn(List.of(livro));
        when(livroAutorRepository.countByLivroCodL(1)).thenReturn(2);

        assertDoesNotThrow(() -> autorService.excluir(autorId));

        verify(livroAutorRepository).deleteByAutorId(autorId);
        verify(autorRepository).deleteById(autorId);
    }

    @Test
    void excluir_deveLancarExcecao_SeAutorUnicoDoLivro() {
        int autorId = 1;
        Autor autor = new Autor();
        autor.setCodAu(autorId);

        Livro livro = new Livro();
        livro.setCodL(1);
        livro.setTitulo("Livro 1");

        when(autorRepository.findById(autorId)).thenReturn(Optional.of(autor));
        when(livroRepository.findByAutorId(autorId)).thenReturn(List.of(livro));
        when(livroAutorRepository.countByLivroCodL(1)).thenReturn(1);

        assertThrows(AutorUniqueForBookException.class, () -> autorService.excluir(autorId));
    }

    @Test
    void atualizar_deveAtualizarAutor() {
        int autorId = 1;
        Autor autorExistente = new Autor();
        autorExistente.setCodAu(autorId);
        autorExistente.setNome("Antigo");

        Autor autorAtualizado = new Autor();
        autorAtualizado.setCodAu(autorId);
        autorAtualizado.setNome("Novo");

        AutorDTO dto = new AutorDTO("Novo");

        when(autorRepository.findById(autorId)).thenReturn(Optional.of(autorExistente));
        when(autorRepository.save(any(Autor.class))).thenReturn(autorAtualizado);

        AutorRetornoDTO resultado = autorService.atualizar(autorId, dto);

        assertEquals("Novo", resultado.getNome());
    }

    @Test
    void buscarPorNome_deveRetornarAutores() {
        Autor autor = new Autor();
        autor.setCodAu(1);
        autor.setNome("João");

        when(autorRepository.findByNomeContainingIgnoreCase("jo")).thenReturn(List.of(autor));

        List<AutorRetornoDTO> lista = autorService.buscarPorNome("jo");

        assertEquals(1, lista.size());
        assertEquals("João", lista.get(0).getNome());
    }

    @Test
    void buscarPorNome_deveLancarExcecao_SeNaoEncontrado() {
        when(autorRepository.findByNomeContainingIgnoreCase("xyz")).thenReturn(Collections.emptyList());

        assertThrows(AutorNotFoundException.class, () -> autorService.buscarPorNome("xyz"));
    }
}