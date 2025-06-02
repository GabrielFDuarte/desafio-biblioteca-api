package com.spassu.livraria.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spassu.livraria.dto.AutorDTO;
import com.spassu.livraria.dto.AutorRetornoDTO;
import com.spassu.livraria.service.AutorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AutorController.class)
class AutorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AutorService autorService;

    @Autowired
    private ObjectMapper objectMapper;

    private AutorDTO autorDTO;
    private AutorRetornoDTO autorRetornoDTO;

    @BeforeEach
    void setUp() {
        autorDTO = new AutorDTO("João");
        autorDTO.setNome("Machado de Assis");

        autorRetornoDTO = new AutorRetornoDTO();
        autorRetornoDTO.setCodAu(1);
        autorRetornoDTO.setNome("Machado de Assis");
    }

    @Test
    void deveCadastrarAutorComSucesso() throws Exception {
        when(autorService.salvar(any(AutorDTO.class))).thenReturn(autorRetornoDTO);

        mockMvc.perform(post("/api/autores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(autorDTO)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/autores/1"))
                .andExpect(jsonPath("$.codAu").value(1))
                .andExpect(jsonPath("$.nome").value("Machado de Assis"));
    }

    @Test
    void deveListarTodosAutoresSemPaginacao() throws Exception {
        List<AutorRetornoDTO> lista = List.of(autorRetornoDTO);
        when(autorService.listarTodos()).thenReturn(lista);

        mockMvc.perform(get("/api/autores"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].codAu").value(1))
                .andExpect(jsonPath("$[0].nome").value("Machado de Assis"));
    }

    @Test
    void deveListarAutoresComPaginacao() throws Exception {
        Page<AutorRetornoDTO> page = new PageImpl<>(List.of(autorRetornoDTO), PageRequest.of(0, 10), 1);
        when(autorService.listarPaginado(0, 10)).thenReturn(page);

        mockMvc.perform(get("/api/autores")
                        .param("pagina", "0")
                        .param("tamanho", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.conteudo[0].codAu").value(1))
                .andExpect(jsonPath("$.conteudo[0].nome").value("Machado de Assis"));
    }

    @Test
    void deveBuscarAutorPorId() throws Exception {
        when(autorService.buscarPorId(1)).thenReturn(autorRetornoDTO);

        mockMvc.perform(get("/api/autores/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.codAu").value(1))
                .andExpect(jsonPath("$.nome").value("Machado de Assis"));
    }

    @Test
    void deveBuscarAutoresPorNome() throws Exception {
        List<AutorRetornoDTO> lista = List.of(autorRetornoDTO);
        when(autorService.buscarPorNome("Machado")).thenReturn(lista);

        mockMvc.perform(get("/api/autores/buscar")
                        .param("nome", "Machado"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].codAu").value(1))
                .andExpect(jsonPath("$[0].nome").value("Machado de Assis"));
    }

    @Test
    void deveExcluirAutor() throws Exception {
        doNothing().when(autorService).excluir(1);

        mockMvc.perform(delete("/api/autores/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deveAtualizarAutor() throws Exception {
        when(autorService.atualizar(eq(1), any(AutorDTO.class))).thenReturn(autorRetornoDTO);

        mockMvc.perform(put("/api/autores/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(autorDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.codAu").value(1))
                .andExpect(jsonPath("$.nome").value("Machado de Assis"));
    }
}