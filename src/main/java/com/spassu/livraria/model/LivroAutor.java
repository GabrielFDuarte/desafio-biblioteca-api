package com.spassu.livraria.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "LIVRO_AUTOR")
@IdClass(LivroAutorId.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LivroAutor {

    @Id
    @ManyToOne
    @JoinColumn(name = "LIVRO_CODL")
    private Livro livro;

    @Id
    @ManyToOne
    @JoinColumn(name = "AUTOR_CODAU")
    private Autor autor;
}
