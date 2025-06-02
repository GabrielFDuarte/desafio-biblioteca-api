package com.spassu.livraria.repository;

import com.spassu.livraria.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AutorRepository extends JpaRepository<Autor, Integer> {

    List<Autor> findByNomeContainingIgnoreCase(String nome);
}
