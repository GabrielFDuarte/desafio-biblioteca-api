package com.spassu.livraria.controller;

import com.spassu.livraria.dto.AssuntoDTO;
import com.spassu.livraria.dto.AssuntoRetornoDTO;
import com.spassu.livraria.dto.PaginaDeAssuntosDTO;
import com.spassu.livraria.service.AssuntoService;
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
@RequestMapping("/api/assuntos")
@Tag(name = "Assunto", description = "Endpoints para gerenciamento de assuntos.")
@RequiredArgsConstructor
public class AssuntoController {

    private final AssuntoService assuntoService;

    @PostMapping
    @Operation(summary = "Cadastra um novo assunto.", description = "Cadastra e retorna o assunto cadastrado.")
    public ResponseEntity<AssuntoRetornoDTO> cadastrar(@RequestBody @Valid AssuntoDTO dto) {
        AssuntoRetornoDTO assuntoRetornoDTO = assuntoService.salvar(dto);
        URI location = URI.create("/api/assuntos/" + assuntoRetornoDTO.getCodAs());
        return ResponseEntity.created(location).body(assuntoRetornoDTO);
    }

    @GetMapping
    @Operation(summary = "Lista os assuntos.", description = "Lista todos os assuntos cadastrados.")
    public ResponseEntity<?> listarTodos(
            @RequestParam(required = false) Integer pagina,
            @RequestParam(required = false) Integer tamanho) {

        if (!Objects.isNull(pagina) && !Objects.isNull(tamanho)) {
            Page<AssuntoRetornoDTO> paginacao = assuntoService.listarPaginado(pagina, tamanho);
            PaginaDeAssuntosDTO resposta = new PaginaDeAssuntosDTO(paginacao);
            return ResponseEntity.ok(resposta);
        } else {
            List<AssuntoRetornoDTO> lista = assuntoService.listarTodos();
            return ResponseEntity.ok(lista);
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca um assunto.", description = "Busca e retorna um assunto pelo seu id.")
    public ResponseEntity<AssuntoRetornoDTO> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(assuntoService.buscarPorId(id));
    }

    @GetMapping("/buscar")
    @Operation(summary = "Busca um assunto.", description = "Busca e retorna um assunto pela sua descrição.")
    public ResponseEntity<List<AssuntoRetornoDTO>> buscarPorDescricao(@RequestParam String descricao) {
        List<AssuntoRetornoDTO> assuntos = assuntoService.buscarPorDescricao(descricao);
        return ResponseEntity.ok(assuntos);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Exclui um assunto.", description = "Apaga um assunto cadastrado.")
    public ResponseEntity<Void> excluir(@PathVariable Integer id) {
        assuntoService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza um assunto.", description = "Atualiza e retorna o assunto atualizado.")
    public ResponseEntity<AssuntoRetornoDTO> atualizar(@PathVariable Integer id, @RequestBody @Valid AssuntoDTO assuntoDTO) {
        return ResponseEntity.ok(assuntoService.atualizar(id, assuntoDTO));
    }
}
