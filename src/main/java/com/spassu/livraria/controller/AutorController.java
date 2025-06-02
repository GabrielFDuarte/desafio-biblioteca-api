package com.spassu.livraria.controller;

import com.spassu.livraria.dto.AutorDTO;
import com.spassu.livraria.dto.AutorRetornoDTO;
import com.spassu.livraria.dto.PaginaDeAutoresDTO;
import com.spassu.livraria.service.AutorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/autores")
@Tag(name = "Autor", description = "Endpoints para gerenciamento de autores.")
@RequiredArgsConstructor
public class AutorController {

    private final AutorService autorService;

    @PostMapping
    @Operation(summary = "Cadastra um novo autor.", description = "Cadastra e retorna o autor cadastrado.")
    public ResponseEntity<AutorRetornoDTO> cadastrar(@RequestBody @Valid AutorDTO dto) {
        AutorRetornoDTO autorRetornoDTO = autorService.salvar(dto);
        URI location = URI.create("/api/autores/" + autorRetornoDTO.getCodAu());
        return ResponseEntity.created(location).body(autorRetornoDTO);
    }

    @GetMapping
    @Operation(summary = "Lista os autores.", description = "Lista todos os autores cadastrados.")
    public ResponseEntity<?> listarTodos(
            @RequestParam(required = false) Integer pagina,
            @RequestParam(required = false) Integer tamanho) {

        if (!Objects.isNull(pagina) && !Objects.isNull(tamanho)) {
            Page<AutorRetornoDTO> paginacao = autorService.listarPaginado(pagina, tamanho);
            PaginaDeAutoresDTO resposta = new PaginaDeAutoresDTO(paginacao);
            return ResponseEntity.ok(resposta);
        } else {
            List<AutorRetornoDTO> lista = autorService.listarTodos();
            return ResponseEntity.ok(lista);
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca um autor.", description = "Busca e retorna um autor pelo seu id.")
    public ResponseEntity<AutorRetornoDTO> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(autorService.buscarPorId(id));
    }

    @GetMapping("/buscar")
    @Operation(summary = "Busca um autor.", description = "Busca e retorna um autor pelo seu nome.")
    public ResponseEntity<List<AutorRetornoDTO>> buscarPorNome(@RequestParam String nome) {
        List<AutorRetornoDTO> autores = autorService.buscarPorNome(nome);
        return ResponseEntity.ok(autores);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Exclui um autor.", description = "Apaga um autor cadastrado.")
    public ResponseEntity<Void> excluir(@PathVariable Integer id) {
        autorService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza um autor.", description = "Atualiza e retorna o autor atualizado.")
    public ResponseEntity<AutorRetornoDTO> atualizar(@PathVariable Integer id, @RequestBody @Valid AutorDTO autorDTO) {
        return ResponseEntity.ok(autorService.atualizar(id, autorDTO));
    }
}
