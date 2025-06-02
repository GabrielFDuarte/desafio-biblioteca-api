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
public class AutorService {

    private final AutorRepository autorRepository;
    private final LivroRepository livroRepository;
    private final LivroAutorRepository livroAutorRepository;

    @Transactional
    public AutorRetornoDTO salvar(AutorDTO dto) {
        Autor autor = new Autor();
        autor.setNome(dto.getNome());

        Autor autorCadastrado = autorRepository.save(autor);
        return toAutorDTO(autorCadastrado);
    }

    public List<AutorRetornoDTO> listarTodos() {
        List<Autor> autores = autorRepository.findAll();
        if (autores.isEmpty()) {
            throw new AutorListEmptyException();
        }
        return autores.stream().map(this::toAutorDTO).toList();
    }

    public Page<AutorRetornoDTO> listarPaginado(int pagina, int tamanho) {
        Pageable pageable = PageRequest.of(pagina, tamanho);
        Page<Autor> paginaResultado = autorRepository.findAll(pageable);

        if (paginaResultado.getTotalElements() == 0) {
            throw new AutorListEmptyException();
        }
        if (!paginaResultado.hasContent()) {
            throw new AutorPageEmptyException(pagina);
        }

        return paginaResultado.map(this::toAutorDTO);
    }

    public AutorRetornoDTO buscarPorId(Integer id) {
        Autor autor = buscarAutorOuLancarExcecao(id);
        return toAutorDTO(autor);
    }

    @Transactional
    public void excluir(Integer id) {
        buscarAutorOuLancarExcecao(id);

        List<Livro> livrosDoAutor = livroRepository.findByAutorId(id);
        for (Livro livro : livrosDoAutor) {
            int quantidadeAutores = livroAutorRepository.countByLivroCodL(livro.getCodL());
            if (quantidadeAutores == 1) {
                throw new AutorUniqueForBookException(livro.getTitulo());
            }
        }

        livroAutorRepository.deleteByAutorId(id);
        autorRepository.deleteById(id);
    }

    @Transactional
    public AutorRetornoDTO atualizar(Integer id, AutorDTO autorAtualizado) {
        Autor autorExistente = buscarAutorOuLancarExcecao(id);

        autorExistente.setNome(autorAtualizado.getNome());
        Autor autorAjustado = autorRepository.save(autorExistente);

        return toAutorDTO(autorAjustado);
    }

    public List<AutorRetornoDTO> buscarPorNome(String nome) {
        List<Autor> listaAutores = autorRepository.findByNomeContainingIgnoreCase(nome);

        if (listaAutores.isEmpty()) {
            throw new AutorNotFoundException(nome);
        }

        return listaAutores.stream().map(this::toAutorDTO).collect(Collectors.toList());
    }

    private Autor buscarAutorOuLancarExcecao(Integer id) {
        return autorRepository.findById(id).orElseThrow(() -> new AutorNotFoundException(id));
    }

    private AutorRetornoDTO toAutorDTO(Autor autor) {
        AutorRetornoDTO autorRetornoDTO = new AutorRetornoDTO();
        autorRetornoDTO.setCodAu(autor.getCodAu());
        autorRetornoDTO.setNome(autor.getNome());

        return autorRetornoDTO;
    }
}
