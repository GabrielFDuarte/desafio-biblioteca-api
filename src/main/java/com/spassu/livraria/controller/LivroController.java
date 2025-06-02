package com.spassu.livraria.controller;

import com.spassu.livraria.dto.LivroDTO;
import com.spassu.livraria.dto.LivroRetornoDTO;
import com.spassu.livraria.dto.PaginaDeLivrosDTO;
import com.spassu.livraria.service.LivroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/livros")
@Tag(name = "Livro", description = "Endpoints para gerenciamento de livros.")
@RequiredArgsConstructor
public class LivroController {

    private final LivroService livroService;

    @PostMapping
    @Operation(summary = "Cadastra um novo livro.", description = "Cadastra e retorna o livro cadastrado.")
    public ResponseEntity<LivroRetornoDTO> cadastrar(@RequestBody @Valid LivroDTO dto) {
        return ResponseEntity.ok(livroService.salvar(dto));
    }

    @GetMapping
    @Operation(summary = "Lista os livros.", description = "Retorna todos os livros cadastrados.")
    public ResponseEntity<?> listarTodos(
            @RequestParam(required = false) Integer pagina,
            @RequestParam(required = false) Integer tamanho) {

        if (!Objects.isNull(pagina) && !Objects.isNull(tamanho)) {
            Page<LivroRetornoDTO> paginacao = livroService.listarPaginado(pagina, tamanho);
            PaginaDeLivrosDTO resposta = new PaginaDeLivrosDTO(paginacao);
            return ResponseEntity.ok(resposta);
        } else {
            List<LivroRetornoDTO> lista = livroService.listarTodos();
            return ResponseEntity.ok(lista);
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca livro por id.", description = "Busca e retorna um livro cadastrado pelo seu id.")
    public ResponseEntity<LivroRetornoDTO> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(livroService.buscarPorId(id));
    }

    @GetMapping("/buscar")
    @Operation(summary = "Busca livro por título.", description = "Busca e retorna um livro cadastrado pelo seu título.")
    public ResponseEntity<List<LivroRetornoDTO>> buscarPorTitulo(@RequestParam String titulo) {
        List<LivroRetornoDTO> livros = livroService.buscarPorTitulo(titulo);
        return ResponseEntity.ok(livros);
    }

    @GetMapping("/autor/{autorId}")
    @Operation(summary = "Busca livro por autor.", description = "Busca e retorna um livro cadastrado pelo seu autor.")
    public List<LivroRetornoDTO> buscarPorAutor(@PathVariable Integer autorId) {
        return livroService.buscarPorAutor(autorId);
    }

    @GetMapping("/assunto/{assuntoId}")
    @Operation(summary = "Busca livro por assunto.", description = "Busca e retorna um livro cadastrado pelo seu assunto.")
    public List<LivroRetornoDTO> buscarPorAssunto(@PathVariable Integer assuntoId) {
        return livroService.buscarPorAssunto(assuntoId);
    }

    @GetMapping("/faixa-preco")
    @Operation(summary = "Busca livro por preço.", description = "Busca e retorna um livro cadastrado por uma faixa de preço.")
    public List<LivroRetornoDTO> buscarPorFaixaDeValor(@RequestParam Integer valorMin, @RequestParam Integer valorMax) {
        return livroService.buscarPorFaixaDeValor(valorMin, valorMax);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Exclui um livro.", description = "Apaga um livro cadastrado.")
    public ResponseEntity<Void> excluir(@PathVariable Integer id) {
        livroService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza um livro.", description = "Atualiza e retorna um livro após atualização pelo seu id.")
    public ResponseEntity<LivroRetornoDTO> atualizar(@PathVariable Integer id, @RequestBody @Valid LivroDTO livroDTO) {
        return ResponseEntity.ok(livroService.atualizar(id, livroDTO));
    }

    @PutMapping("/{id}/atualiza-autores")
    @Operation(summary = "Atualiza autores do livro.", description = "Atualiza e retorna um livro após atualização dos seus autores.")
    public ResponseEntity<LivroRetornoDTO> vincularDesvincularAutores(@PathVariable Integer id, @RequestBody List<Integer> autoresIds) {
        return ResponseEntity.ok(livroService.vincularDesvincularAutores(id, autoresIds));
    }

    @PutMapping("/{id}/atualiza-assuntos")
    @Operation(summary = "Atualiza assuntos do livro.", description = "Atualiza e retorna um livro após atualização dos seus assuntos.")
    public ResponseEntity<LivroRetornoDTO> vincularDesvincularAssuntos(@PathVariable Integer id, @RequestBody List<Integer> assuntosIds) {
        return ResponseEntity.ok(livroService.vincularDesvincularAssuntos(id, assuntosIds));
    }
}
