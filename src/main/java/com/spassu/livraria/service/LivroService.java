package com.spassu.livraria.service;

import com.spassu.livraria.dto.AssuntoRetornoDTO;
import com.spassu.livraria.dto.AutorRetornoDTO;
import com.spassu.livraria.dto.LivroDTO;
import com.spassu.livraria.dto.LivroRetornoDTO;
import com.spassu.livraria.exception.*;
import com.spassu.livraria.model.Assunto;
import com.spassu.livraria.model.Autor;
import com.spassu.livraria.model.Livro;
import com.spassu.livraria.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Year;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class LivroService {

    private final LivroRepository livroRepository;
    private final AutorRepository autorRepository;
    private final AssuntoRepository assuntoRepository;
    private final LivroAutorRepository livroAutorRepository;
    private final LivroAssuntoRepository livroAssuntoRepository;

    @Transactional
    public LivroRetornoDTO salvar(LivroDTO dto) {
        validarAnoPublicacao(dto.getAnoPublicacao());

        dto.setAutoresIds(removeDuplicados(dto.getAutoresIds()));
        dto.setAssuntosIds(removeDuplicados(dto.getAssuntosIds()));

        List<Integer> autoresIds = dto.getAutoresIds();
        if (Objects.isNull(autoresIds) || autoresIds.isEmpty()) {
            throw new LivroParamErrorException(autoresIds);
        }

        List<Integer> assuntosIds = dto.getAssuntosIds();
        if (Objects.isNull(assuntosIds) || assuntosIds.isEmpty()) {
            throw new LivroParamErrorException(assuntosIds);
        }

        Livro livro = new Livro();
        livro.setTitulo(dto.getTitulo());
        livro.setEditora(dto.getEditora());
        livro.setEdicao(dto.getEdicao());
        livro.setAnoPublicacao(dto.getAnoPublicacao());

        if (!Objects.isNull(dto.getValor())) {
            livro.setValor((int) Math.round(dto.getValor() * 100));
        }

        List<Autor> autores = validarEObterAutores(autoresIds);

        List<Assunto> assuntos = assuntoRepository.findAllById(assuntosIds);
        if (assuntos.size() != assuntosIds.size()) {
            throw new LivroParamErrorException();
        }

        livro.setAutores(autores);
        livro.setAssuntos(assuntos);

        return toLivroDTO(livroRepository.save(livro));
    }

    public List<LivroRetornoDTO> listarTodos() {
        List<Livro> livros = livroRepository.findAll();
        if (livros.isEmpty()) {
            throw new LivroListEmptyException();
        }
        return livros.stream().map(this::toLivroDTO).toList();
    }

    public Page<LivroRetornoDTO> listarPaginado(int pagina, int tamanho) {
        Pageable pageable = PageRequest.of(pagina, tamanho);
        Page<Livro> paginaResultado = livroRepository.findAll(pageable);

        if (paginaResultado.getTotalElements() == 0) {
            throw new LivroListEmptyException();
        }
        if (!paginaResultado.hasContent()) {
            throw new LivroPageEmptyException(pagina);
        }

        return paginaResultado.map(this::toLivroDTO);
    }

    public LivroRetornoDTO buscarPorId(Integer id) {
        Livro livro = livroRepository.findById(id).orElseThrow(() -> new LivroNotFoundException(id));
        return toLivroDTO(livro);
    }

    @Transactional
    public void excluir(Integer id) {
        if (!livroRepository.existsById(id)) {
            throw new LivroNotFoundException(id);
        }

        livroAutorRepository.deleteByLivroId(id);
        livroAssuntoRepository.deleteByLivroId(id);
        livroRepository.deleteById(id);
    }

    @Transactional
    public LivroRetornoDTO atualizar(Integer id, LivroDTO livroAtualizado) {
        validarAnoPublicacao(livroAtualizado.getAnoPublicacao());

        livroAtualizado.setAutoresIds(removeDuplicados(livroAtualizado.getAutoresIds()));
        livroAtualizado.setAssuntosIds(removeDuplicados(livroAtualizado.getAssuntosIds()));

        List<Integer> autoresIds = livroAtualizado.getAutoresIds();
        if (Objects.isNull(autoresIds) || autoresIds.isEmpty()) {
            throw new LivroParamErrorException(autoresIds);
        }

        List<Integer> assuntosIds = livroAtualizado.getAssuntosIds();
        if (Objects.isNull(assuntosIds) || assuntosIds.isEmpty()) {
            throw new LivroParamErrorException(assuntosIds);
        }

        Livro livroExistente = livroRepository.findById(id).orElseThrow(() -> new LivroNotFoundException(id));

        livroExistente.setTitulo(livroAtualizado.getTitulo());
        livroExistente.setEditora(livroAtualizado.getEditora());
        livroExistente.setEdicao(livroAtualizado.getEdicao());
        livroExistente.setAnoPublicacao(livroAtualizado.getAnoPublicacao());

        if (!Objects.isNull(livroAtualizado.getValor())) {
            livroExistente.setValor((int) Math.round(livroAtualizado.getValor() * 100));
        }

        List<Autor> autores = validarEObterAutores(autoresIds);

        List<Assunto> assuntos = assuntoRepository.findAllById(assuntosIds);
        if (assuntos.size() != assuntosIds.size()) {
            throw new LivroParamErrorException();
        }

        List<Assunto> assuntosAtualizados = assuntoRepository.findAllById(livroAtualizado.getAssuntosIds());

        livroExistente.setAutores(autores);
        livroExistente.setAssuntos(assuntosAtualizados);

        Livro livro = livroRepository.save(livroExistente);
        return toLivroDTO(livro);
    }

    @Transactional
    public LivroRetornoDTO vincularDesvincularAutores(Integer livroId, List<Integer> autoresIds) {
        Livro livro = livroRepository.findById(livroId).orElseThrow(() -> new LivroNotFoundException(livroId));

        if (Objects.isNull(autoresIds) || autoresIds.isEmpty()) {
            throw new AutorUniqueForBookException(livro.getTitulo(), autoresIds);
        }

        List<Integer> idsUnicos = removeDuplicados(autoresIds);

        List<Autor> autores = autorRepository.findAllById(idsUnicos);
        if (autores.size() != idsUnicos.size()) {
            throw new LivroParamErrorException();
        }

        livro.setAutores(autores);

        Livro livroAjustado = livroRepository.save(livro);
        return toLivroDTO(livroAjustado);
    }

    @Transactional
    public LivroRetornoDTO vincularDesvincularAssuntos(Integer livroId, List<Integer> assuntosIds) {
        Livro livro = livroRepository.findById(livroId).orElseThrow(() -> new LivroNotFoundException(livroId));

        if (Objects.isNull(assuntosIds) || assuntosIds.isEmpty()) {
            throw new AssuntoUniqueForBookException(livro.getTitulo(), assuntosIds);
        }

        List<Integer> idsUnicos = removeDuplicados(assuntosIds);

        List<Assunto> assuntos = assuntoRepository.findAllById(idsUnicos);
        if (assuntos.size() != idsUnicos.size()) {
            throw new LivroParamErrorException();
        }

        livro.setAssuntos(assuntos);

        Livro livroAjustado = livroRepository.save(livro);
        return toLivroDTO(livroAjustado);
    }

    public List<LivroRetornoDTO> buscarPorTitulo(String titulo) {
        List<Livro> listaLivros = livroRepository.findByTituloContainingIgnoreCase(titulo);

        if (listaLivros.isEmpty()) {
            throw new LivroNotFoundException(titulo);
        }

        return listaLivros.stream().map(this::toLivroDTO).collect(Collectors.toList());
    }

    public List<LivroRetornoDTO> buscarPorAutor(Integer autorId) {
        Autor autor = autorRepository.findById(autorId).orElseThrow(() -> new AutorNotFoundException(autorId));

        List<Livro> livros = livroRepository.findByAutorId(autorId);

        if (livros.isEmpty()) {
            throw new LivroNotFoundException(autor.getNome());
        }

        return livros.stream().map(this::toLivroDTO).collect(Collectors.toList());
    }

    public List<LivroRetornoDTO> buscarPorAssunto(Integer assuntoId) {
        Assunto assunto = assuntoRepository.findById(assuntoId).orElseThrow(() -> new AssuntoNotFoundException(assuntoId));

        List<Livro> livros = livroRepository.findByAssuntoId(assuntoId);

        if (livros.isEmpty()) {
            throw new LivroNotFoundException(assunto.getDescricao());
        }

        return livros.stream().map(this::toLivroDTO).collect(Collectors.toList());
    }

    public List<LivroRetornoDTO> buscarPorFaixaDeValor(Integer valorMin, Integer valorMax) {
        if (Objects.isNull(valorMin) || Objects.isNull(valorMax)) {
            throw new LivroParamValorException();
        }

        if (valorMin.compareTo(valorMax) > 0) {
            throw new LivroParamValorException(valorMin, valorMax);
        }

        List<Livro> livros = livroRepository.findByValorBetween(valorMin, valorMax);

        if (livros.isEmpty()) {
            throw new LivroNotFoundException(valorMin, valorMax);
        }

        return livros.stream().map(this::toLivroDTO).collect(Collectors.toList());
    }

    private LivroRetornoDTO toLivroDTO(Livro livro) {
        LivroRetornoDTO livroDto = new LivroRetornoDTO();
        livroDto.setCodL(livro.getCodL());
        livroDto.setTitulo(livro.getTitulo());
        livroDto.setEditora(livro.getEditora());
        livroDto.setEdicao(livro.getEdicao());
        livroDto.setAnoPublicacao(livro.getAnoPublicacao());

        if (!Objects.isNull(livro.getValor())) {
            livroDto.setValor(livro.getValor() / 100.00);
        }

        if (livro.getAutores() != null) {
            List<AutorRetornoDTO> autoresDto = livro.getAutores().stream()
                    .map(autor -> {
                        AutorRetornoDTO dto = new AutorRetornoDTO();
                        dto.setCodAu(autor.getCodAu());
                        dto.setNome(autor.getNome());
                        return dto;
                    })
                    .toList();
            livroDto.setAutores(autoresDto);
        }

        if (livro.getAssuntos() != null) {
            List<AssuntoRetornoDTO> assuntosDto = livro.getAssuntos().stream()
                    .map(assunto -> {
                        AssuntoRetornoDTO dto = new AssuntoRetornoDTO();
                        dto.setCodAs(assunto.getCodAs());
                        dto.setDescricao(assunto.getDescricao());
                        return dto;
                    })
                    .toList();
            livroDto.setAssuntos(assuntosDto);
        }

        return livroDto;
    }

    private void validarAnoPublicacao(String anoStr) {
        int anoAtual = Year.now().getValue();

        try {
            int ano = Integer.parseInt(anoStr);
            if (ano > anoAtual) {
                throw new LivroParamErrorException(anoStr, anoAtual);
            }
        } catch (NumberFormatException e) {
            log.error("Erro ao converter ano '{}' para {}", anoStr, e.getMessage());
            throw new LivroParamErrorException(anoStr);
        }
    }

    private List<Autor> validarEObterAutores(List<Integer> ids) {
        List<Autor> autores = autorRepository.findAllById(ids);
        if (autores.size() != ids.size()) {
            throw new LivroParamErrorException();
        }
        return autores;
    }

    private List<Integer> removeDuplicados(List<Integer> ids) {
        return new ArrayList<>(new HashSet<>(ids));
    }
}
