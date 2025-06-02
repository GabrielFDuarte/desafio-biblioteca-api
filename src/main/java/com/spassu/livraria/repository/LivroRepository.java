package com.spassu.livraria.repository;

import com.spassu.livraria.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LivroRepository extends JpaRepository<Livro, Integer> {

    List<Livro> findByTituloContainingIgnoreCase(String titulo);

    @Query("SELECT l FROM Livro l JOIN LivroAutor la ON la.livro.codL = l.codL WHERE la.autor.codAu = :autorId")
    List<Livro> findByAutorId(Integer autorId);

    @Query("SELECT l FROM Livro l JOIN LivroAssunto la ON la.livro.codL = l.codL WHERE la.assunto.codAs = :assuntoId")
    List<Livro> findByAssuntoId(Integer assuntoId);

    List<Livro> findByValorBetween(Integer valorMin, Integer valorMax);
}
