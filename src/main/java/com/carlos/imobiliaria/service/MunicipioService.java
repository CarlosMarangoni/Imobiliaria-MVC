package com.carlos.imobiliaria.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.carlos.imobiliaria.exception.EntidadeEmUsoException;
import com.carlos.imobiliaria.filter.FilterDesc;
import com.carlos.imobiliaria.model.Municipio;
import com.carlos.imobiliaria.repository.MunicipioRepository;
/*Author: Carlos Eduardo Marangoni Mendes


*/
@Service
public class MunicipioService {

	@Autowired
	private MunicipioRepository municipioRepository;

	public void salvar(Municipio municipio) {
		municipioRepository.save(municipio);
	}

	public void excluir(Long codigo) {
		try {
			municipioRepository.deleteById(codigo); // Exception para não violar a integridade
		} catch (DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(String.format(
					"Município não pode ser excluído pois está em uso por um bairro. Favor excluir o bairro do respectivo município antes de excluir um município."));
		}

	}

	public Page<Municipio> filtrarPage(int pageNo, int pageSize, FilterDesc filtro, String radioFilter) {
		String descricao = filtro.getDescricao() == null ? "" : filtro.getDescricao();
		Pageable pageable = PageRequest.of(pageNo - 1, pageSize);

		switch (radioFilter) {
		case "estado":
			return municipioRepository.findByEstadoNomeContaining(descricao, pageable); // Verifica o parâmetro
																						// radioFilter na URL de
																						// requisição e retorna o filtro
																						// conforme selecionado.
		default:
			return municipioRepository.findByNomeContaining(descricao, pageable);
		}

	}

	public String urlParams(FilterDesc filtro, String radioFilter) {
		String descricao = filtro.getDescricao() == null ? "" : filtro.getDescricao();
		return "descricao=" + descricao + "&radioFilter=" + radioFilter; // parâmetros da URL com os filtros para mandar
																			// para a VIEW, assim podendo mudar de
																			// página os filtros permanecerem
	}

	public List<Municipio> encontrarMunicipiosPorEstado(Long codigoEstado) { // Retorna uma lista com todos os
																				// municipios com o código do parâmetro.
		return municipioRepository.findByEstadoCodigo(codigoEstado);

	}
}
