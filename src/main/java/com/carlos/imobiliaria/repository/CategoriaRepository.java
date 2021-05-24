package com.carlos.imobiliaria.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.carlos.imobiliaria.model.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
	public Page<Categoria> findByNomeContaining(String descricao, Pageable pageable);
}
