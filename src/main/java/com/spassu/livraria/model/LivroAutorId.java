package com.spassu.livraria.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
public class LivroAutorId implements Serializable {

    private Integer livro;
    private Integer autor;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        LivroAutorId that = (LivroAutorId) o;
        return Objects.equals(livro, that.livro) && Objects.equals(autor, that.autor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(livro, autor);
    }
}
