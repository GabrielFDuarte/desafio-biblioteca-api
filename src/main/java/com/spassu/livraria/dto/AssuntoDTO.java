package com.spassu.livraria.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AssuntoDTO {

    @NotBlank(message = "A descrição do Assunto é obrigatória.")
    @Size(max = 20, message = "A descrição não pode ter mais de 20 caracteres.")
    private String descricao;
}
