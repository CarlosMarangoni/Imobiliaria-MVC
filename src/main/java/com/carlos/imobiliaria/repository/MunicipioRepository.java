package com.carlos.imobiliaria.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.carlos.imobiliaria.model.Municipio;

public interface MunicipioRepository extends JpaRepository<Municipio, Long> {

	public Page<Municipio> findByNomeContaining(String descricao, Pageable pageable);

	public Page<Municipio> findByEstadoNomeContaining(String descricao, Pageable pageable);

	public List<Municipio> findByEstadoCodigo(Long codigo);
}
