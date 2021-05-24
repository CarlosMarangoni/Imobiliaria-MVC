package com.carlos.imobiliaria.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.carlos.imobiliaria.exception.EntidadeEmUsoException;
import com.carlos.imobiliaria.filter.FilterDesc;
import com.carlos.imobiliaria.model.Estado;
import com.carlos.imobiliaria.repository.EstadoRepository;
/*Author: Carlos Eduardo Marangoni Mendes


*/
@Service
public class EstadoService {

	@Autowired
	private EstadoRepository estadoRepository;

	public void salvar(Estado estado) {
		estadoRepository.save(estado);
	}

	public void excluir(Long codigo) {
		try {

			estadoRepository.deleteById(codigo);
		} catch (DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(String.format(
					"Estado não pode ser excluído pois está em uso por um município ou bairro. Favor excluir o município/bairro com o respectivo estado antes de excluir um estado."));
		}
	}

	public Page<Estado> filtrarPage(int pageNo, int pageSize, FilterDesc filtro) {
		String descricao = filtro.getDescricao() == null ? "" : filtro.getDescricao();
		Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
		return estadoRepository.findByNomeContaining(descricao, pageable);

	}

	public String urlParams(FilterDesc filtro, String radioFilter) {
		String descricao = filtro.getDescricao() == null ? "" : filtro.getDescricao();
		return "descricao=" + descricao + "&radioFilter=" + radioFilter; // parâmetros da URL com os filtros para mandar
																			// para a VIEW, assim podendo mudar de
																			// página os filtros permanecerem
	}
}
