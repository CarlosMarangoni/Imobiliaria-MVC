package com.carlos.imobiliaria.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.carlos.imobiliaria.model.Negocio;

public interface NegocioRepository extends JpaRepository<Negocio, Long> {
	public Page<Negocio> findByNomeContaining(String descricao, Pageable pageable);
}
