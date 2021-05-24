package com.carlos.imobiliaria.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.carlos.imobiliaria.exception.EntidadeEmUsoException;
import com.carlos.imobiliaria.filter.FilterDesc;
import com.carlos.imobiliaria.model.Categoria;
import com.carlos.imobiliaria.repository.CategoriaRepository;
/*Author: Carlos Eduardo Marangoni Mendes


*/
@Service
public class CategoriaService {

	@Autowired
	private CategoriaRepository categoriaRepository;

	public void salvar(Categoria categoria) {
		categoriaRepository.save(categoria);
	}

	public void excluir(Long codigo) {
		try {

			categoriaRepository.deleteById(codigo);
		} catch (DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(String.format(
					"Categoria não pode ser excluída pois está em uso por um imóvel. Favor excluir o imóvel com a respectiva categoria antes de excluir uma categoria."));
		}
	}

	public Page<Categoria> filtrarPage(int pageNo, int pageSize, FilterDesc filtro) {
		String descricao = filtro.getDescricao() == null ? "" : filtro.getDescricao();
		Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
		return categoriaRepository.findByNomeContaining(descricao, pageable);

	}

	public String urlParams(FilterDesc filtro, String radioFilter) {
		String descricao = filtro.getDescricao() == null ? "" : filtro.getDescricao();
		return "descricao=" + descricao + "&radioFilter=" + radioFilter; // parâmetros da URL com os filtros para mandar
																			// para a VIEW, assim podendo mudar de
																			// página os filtros permanecerem
	}
}
