package com.spassu.livraria.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.List;

@Data
public class LivroDTO {

    @NotBlank(message = "O título do Livro é obrigatório.")
    @Size(max = 40, message = "O título não pode ter mais de 40 caracteres.")
    private String titulo;

    @NotBlank(message = "A editora do Livro é obrigatória.")
    @Size(max = 40, message = "O nome da editora não pode ter mais de 40 caracteres.")
    private String editora;

    @NotNull(message = "A edição do Livro é obrigatória.")
    private Integer edicao;

    @NotBlank(message = "O ano de publicação do Livro é obrigatório.")
    @Pattern(regexp = "\\d{4}", message = "Parâmetro anoPublicacao deve conter exatamente 4 dígitos numéricos.")
    private String anoPublicacao;

    @NotNull(message = "O valor do Livro é obrigatório.")
    private Double valor;

    @NotEmpty(message = "ID(s) de autor(es) do Livro é(são) obrigatório(s).")
    private List<Integer> autoresIds;

    @NotEmpty(message = "ID(s) de assunto(s) do Livro é(são) obrigatório(s).")
    private List<Integer> assuntosIds;
}
