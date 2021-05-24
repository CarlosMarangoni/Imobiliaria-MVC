package com.carlos.imobiliaria.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.carlos.imobiliaria.model.Estado;

public interface EstadoRepository extends JpaRepository<Estado, Long> {
	public Page<Estado> findByNomeContaining(String descricao, Pageable pageable);
}
