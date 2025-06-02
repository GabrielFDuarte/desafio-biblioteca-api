package com.spassu.livraria.repository;

import com.spassu.livraria.model.LivroAutor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface LivroAutorRepository extends JpaRepository<LivroAutor, Long> {

    @Transactional
    @Modifying
    @Query("DELETE FROM LivroAutor la WHERE la.autor.codAu = :autorId")
    void deleteByAutorId(Integer autorId);

    @Transactional
    @Modifying
    @Query("DELETE FROM LivroAutor la WHERE la.livro.codL = :livroId")
    void deleteByLivroId(Integer livroId);

    int countByLivroCodL(Integer codL);
}
