package com.spassu.livraria.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
public class LivroAssuntoId implements Serializable {

    private Integer livro;
    private Integer assunto;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        LivroAssuntoId that = (LivroAssuntoId) o;
        return Objects.equals(livro, that.livro) && Objects.equals(assunto, that.assunto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(livro, assunto);
    }
}
