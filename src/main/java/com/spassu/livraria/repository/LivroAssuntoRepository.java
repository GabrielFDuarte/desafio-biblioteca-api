package com.spassu.livraria.repository;

import com.spassu.livraria.model.LivroAssunto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface LivroAssuntoRepository extends JpaRepository<LivroAssunto, Long> {

    @Transactional
    @Modifying
    @Query("DELETE FROM LivroAssunto la WHERE la.assunto.codAs = :assuntoId")
    void deleteByAssuntoId(Integer assuntoId);

    @Transactional
    @Modifying
    @Query("DELETE FROM LivroAssunto la WHERE la.livro.codL = :livroId")
    void deleteByLivroId(Integer livroId);

    int countByLivroCodL(Integer codL);
}
