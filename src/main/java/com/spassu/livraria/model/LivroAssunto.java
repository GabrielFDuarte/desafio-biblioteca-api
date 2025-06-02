package com.spassu.livraria.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "LIVRO_ASSUNTO")
@IdClass(LivroAssuntoId.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LivroAssunto {

    @Id
    @ManyToOne
    @JoinColumn(name = "LIVRO_CODL")
    private Livro livro;

    @Id
    @ManyToOne
    @JoinColumn(name = "ASSUNTO_CODAS")
    private Assunto assunto;
}
