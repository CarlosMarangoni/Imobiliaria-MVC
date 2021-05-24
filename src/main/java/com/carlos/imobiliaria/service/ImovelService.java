package com.carlos.imobiliaria.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.carlos.imobiliaria.filter.FilterDesc;
import com.carlos.imobiliaria.model.Imovel;
import com.carlos.imobiliaria.repository.ImovelRepository;
/*Author: Carlos Eduardo Marangoni Mendes


*/
@Service
public class ImovelService {

	@Autowired
	private ImovelRepository imovelRepository;

	public void salvarComArquivo(Imovel imovel, MultipartFile multipartFile) throws IOException {

		String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
		imovel.setImagem(fileName);
		imovelRepository.save(imovel);
		String uploadDir = "./imagens-imoveis/" + imovel.getCodigo();

		Path uploadPath = Paths.get(uploadDir);

		if (!Files.exists(uploadPath)) {
			Files.createDirectories(uploadPath);
		}

		try {

			InputStream inputStream = multipartFile.getInputStream();
			Path filePath = uploadPath.resolve(fileName);
			Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			throw new IOException("Não foi possível fazer o upload do arquivo: " + fileName);
		}
	} 

	public void salvar(Imovel imovel) {
		imovelRepository.save(imovel);
	}

	public void excluir(Long codigo) {
		imovelRepository.deleteById(codigo);

	}

	public Page<Imovel> findPaginated(int pageNo, int pageSize) {
		Pageable pageable = PageRequest.of(pageNo - 1, pageSize);

		return this.imovelRepository.findAll(pageable);
	}

	public Page<Imovel> filtrarPage(int pageNo, int pageSize, FilterDesc filtro, String radioFilter) {
		String descricao = filtro.getDescricao() == null ? "" : filtro.getDescricao();
		Pageable pageable = PageRequest.of(pageNo - 1, pageSize);

		switch (radioFilter) {
		case "quartos":
			return imovelRepository.findByQtdQuartos(Integer.parseInt(descricao), pageable);
		case "categoria":
			return imovelRepository.findByCategoriaNomeContaining(descricao, pageable);
		case "negocio":
			return imovelRepository.findByNegocioNomeContaining(descricao, pageable); // Verifica o parâmetro na URL de
																						// requisição e retorna o filtro
																						// conforme selecionado.
		case "estado":
			return imovelRepository.findByEstadoNomeContaining(descricao, pageable);
		case "municipio":
			return imovelRepository.findByMunicipioNomeContaining(descricao, pageable);
		default:
			return imovelRepository.findByDescricaoContaining(descricao, pageable);
		}

	}

	public String urlParams(FilterDesc filtro, String radioFilter) {
		String descricao = filtro.getDescricao() == null ? "" : filtro.getDescricao();
		return "descricao=" + descricao + "&radioFilter=" + radioFilter; // parâmetros da URL com os filtros para mandar
																			// para a VIEW, assim podendo mudar de
																			// página os filtros permanecerem
	}

}
