package com.spassu.livraria.repository;

import com.spassu.livraria.model.Assunto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AssuntoRepository extends JpaRepository<Assunto, Integer> {

    List<Assunto> findByDescricaoContainingIgnoreCase(String descricao);
}
