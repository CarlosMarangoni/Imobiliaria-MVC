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
import com.carlos.imobiliaria.model.Bairro;
import com.carlos.imobiliaria.repository.BairroRepository;
/*Author: Carlos Eduardo Marangoni Mendes


*/
@Service
public class BairroService {

	@Autowired
	private BairroRepository bairroRepository;

	public void salvar(Bairro bairro) {
		bairroRepository.save(bairro);
	}

	public void excluir(Long codigo) {
		try {

			bairroRepository.deleteById(codigo);
		} catch (DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(String.format(
					"Bairro não pode ser excluído pois está em uso por um imóvel. Favor excluir o respectivo imóvel antes de excluir um bairro."));
		}

	}

	public Page<Bairro> filtrarPage(int pageNo, int pageSize, FilterDesc filtro, String radioFilter) {
		String descricao = filtro.getDescricao() == null ? "" : filtro.getDescricao();
		Pageable pageable = PageRequest.of(pageNo - 1, pageSize);

		switch (radioFilter) {
		case "estado":
			return bairroRepository.findByEstadoNomeContaining(descricao, pageable);
		case "municipio":
			return bairroRepository.findByMunicipioNomeContaining(descricao, pageable); // Verifica o parâmetro
																						// radioFilter na URL de
																						// requisição e retorna o filtro
																						// conforme selecionado.
		default:
			return bairroRepository.findByNomeContaining(descricao, pageable);
		}

	}

	public String urlParams(FilterDesc filtro, String radioFilter) {
		String descricao = filtro.getDescricao() == null ? "" : filtro.getDescricao();
		return "descricao=" + descricao + "&radioFilter=" + radioFilter; // parâmetros da URL com os filtros para mandar
																			// para a VIEW, assim podendo mudar de
																			// página os filtros permanecerem
	}

	public List<Bairro> encontrarBairroPorMunicipio(Long codigoMunicipio) { // Retorna uma lista com todos os bairros
																			// com o código do parâmetro.
		return bairroRepository.findByMunicipioCodigo(codigoMunicipio);
	}

}
