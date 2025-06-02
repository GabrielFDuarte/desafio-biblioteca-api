package com.spassu.livraria.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AutorDTO {

    @NotBlank(message = "O nome do Autor é obrigatório.")
    @Size(max = 40, message = "O nome não pode ter mais de 40 caracteres.")
    private String nome;
}
