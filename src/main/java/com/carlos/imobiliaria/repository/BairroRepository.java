package com.carlos.imobiliaria.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.carlos.imobiliaria.model.Bairro;

public interface BairroRepository extends JpaRepository<Bairro, Long> {

	public Page<Bairro> findByNomeContaining(String descricao, Pageable pageable);

	public Page<Bairro> findByEstadoNomeContaining(String descricao, Pageable pageable);

	public Page<Bairro> findByMunicipioNomeContaining(String descricao, Pageable pageable);

	public List<Bairro> findByMunicipioCodigo(Long codigo);
}
