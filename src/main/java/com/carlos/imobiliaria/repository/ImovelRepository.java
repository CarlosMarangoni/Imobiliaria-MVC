package com.carlos.imobiliaria.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.carlos.imobiliaria.model.Imovel;

public interface ImovelRepository extends JpaRepository<Imovel, Long> {

	public Page<Imovel> findByDescricaoContaining(String descricao, Pageable pageable);

	public Page<Imovel> findByQtdQuartos(int descricao, Pageable pageable);

	public Page<Imovel> findByCategoriaNomeContaining(String descricao, Pageable pageable);

	public Page<Imovel> findByNegocioNomeContaining(String descricao, Pageable pageable);

	public Page<Imovel> findByEstadoNomeContaining(String descricao, Pageable pageable);

	public Page<Imovel> findByMunicipioNomeContaining(String descricao, Pageable pageable);

}
