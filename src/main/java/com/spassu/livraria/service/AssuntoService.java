package com.spassu.livraria.service;

import com.spassu.livraria.dto.AssuntoDTO;
import com.spassu.livraria.dto.AssuntoRetornoDTO;
import com.spassu.livraria.exception.*;
import com.spassu.livraria.model.Assunto;
import com.spassu.livraria.model.Livro;
import com.spassu.livraria.repository.AssuntoRepository;
import com.spassu.livraria.repository.LivroAssuntoRepository;
import com.spassu.livraria.repository.LivroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AssuntoService {

    private final AssuntoRepository assuntoRepository;
    private final LivroRepository livroRepository;
    private final LivroAssuntoRepository livroAssuntoRepository;

    @Transactional
    public AssuntoRetornoDTO salvar(AssuntoDTO dto) {
        Assunto assunto = new Assunto();
        assunto.setDescricao(dto.getDescricao());

        Assunto assuntoCadastrado = assuntoRepository.save(assunto);
        return toAssuntoDTO(assuntoCadastrado);
    }

    public List<AssuntoRetornoDTO> listarTodos() {
        List<Assunto> assuntos = assuntoRepository.findAll();
        if (assuntos.isEmpty()) {
            throw new AssuntoListEmptyException();
        }
        return assuntos.stream().map(this::toAssuntoDTO).toList();
    }

    public Page<AssuntoRetornoDTO> listarPaginado(int pagina, int tamanho) {
        Pageable pageable = PageRequest.of(pagina, tamanho);
        Page<Assunto> paginaResultado = assuntoRepository.findAll(pageable);

        if (paginaResultado.getTotalElements() == 0) {
            throw new AssuntoListEmptyException();
        }
        if (!paginaResultado.hasContent()) {
            throw new AssuntoPageEmptyException(pagina);
        }

        return paginaResultado.map(this::toAssuntoDTO);
    }

    public AssuntoRetornoDTO buscarPorId(Integer id) {
        Assunto assunto = buscarOuLancarExcecao(id);
        return toAssuntoDTO(assunto);
    }

    @Transactional
    public void excluir(Integer id) {
        buscarOuLancarExcecao(id);

        List<Livro> livrosDoAssunto = livroRepository.findByAssuntoId(id);
        for (Livro livro : livrosDoAssunto) {
            int quantidadeAssuntos = livroAssuntoRepository.countByLivroCodL(livro.getCodL());
            if (quantidadeAssuntos == 1) {
                throw new AssuntoUniqueForBookException(livro.getTitulo());
            }
        }

        livroAssuntoRepository.deleteByAssuntoId(id);
        assuntoRepository.deleteById(id);
    }

    @Transactional
    public AssuntoRetornoDTO atualizar(Integer id, AssuntoDTO assuntoAtualizado) {
        Assunto assuntoExistente = buscarOuLancarExcecao(id);

        assuntoExistente.setDescricao(assuntoAtualizado.getDescricao());
        Assunto assuntoAjustado = assuntoRepository.save(assuntoExistente);

        return toAssuntoDTO(assuntoAjustado);
    }

    public List<AssuntoRetornoDTO> buscarPorDescricao(String descricao) {
        List<Assunto> listaAssuntos = assuntoRepository.findByDescricaoContainingIgnoreCase(descricao);

        if (listaAssuntos.isEmpty()) {
            throw new AssuntoNotFoundException(descricao);
        }

        return listaAssuntos.stream().map(this::toAssuntoDTO).collect(Collectors.toList());
    }

    private Assunto buscarOuLancarExcecao(Integer id) {
        return assuntoRepository.findById(id).orElseThrow(() -> new AssuntoNotFoundException(id));
    }

    private AssuntoRetornoDTO toAssuntoDTO(Assunto assunto) {
        AssuntoRetornoDTO assuntoRetornoDTO = new AssuntoRetornoDTO();
        assuntoRetornoDTO.setCodAs(assunto.getCodAs());
        assuntoRetornoDTO.setDescricao(assunto.getDescricao());

        return assuntoRetornoDTO;
    }
}
