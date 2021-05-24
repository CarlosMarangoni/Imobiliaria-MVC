package com.carlos.imobiliaria.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.carlos.imobiliaria.exception.EntidadeEmUsoException;
import com.carlos.imobiliaria.filter.FilterDesc;
import com.carlos.imobiliaria.model.Negocio;
import com.carlos.imobiliaria.repository.NegocioRepository;
/*Author: Carlos Eduardo Marangoni Mendes


*/
@Service
public class NegocioService {

	@Autowired
	private NegocioRepository negocioRepository;

	public void salvar(Negocio negocio) {
		negocioRepository.save(negocio);
	}

	public void excluir(Long codigo) {
		try {

			negocioRepository.deleteById(codigo);
		} catch (DataIntegrityViolationException e) {// Exception de violação de integridade
			throw new EntidadeEmUsoException(String.format(
					"Negócio não pode ser excluído pois está em uso por um imóvel. Favor excluir o imóvel com o respectivo negócio antes de excluir um negócio."));
		}
	}

	public Page<Negocio> filtrarPage(int pageNo, int pageSize, FilterDesc filtro) {
		String descricao = filtro.getDescricao() == null ? "" : filtro.getDescricao();
		Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
		return negocioRepository.findByNomeContaining(descricao, pageable);

	}

	public String urlParams(FilterDesc filtro, String radioFilter) {
		String descricao = filtro.getDescricao() == null ? "" : filtro.getDescricao();
		return "descricao=" + descricao + "&radioFilter=" + radioFilter; // parâmetros da URL com os filtros para mandar
																			// para a VIEW, assim podendo mudar de
																			// página os filtros permanecerem
	}

}
