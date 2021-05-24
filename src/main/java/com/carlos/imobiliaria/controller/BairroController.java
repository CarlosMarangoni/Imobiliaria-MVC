package com.carlos.imobiliaria.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.carlos.imobiliaria.exception.EntidadeEmUsoException;
import com.carlos.imobiliaria.filter.FilterDesc;
import com.carlos.imobiliaria.model.Bairro;
import com.carlos.imobiliaria.model.Estado;
import com.carlos.imobiliaria.model.Municipio;
import com.carlos.imobiliaria.repository.EstadoRepository;
import com.carlos.imobiliaria.repository.MunicipioRepository;
import com.carlos.imobiliaria.service.BairroService;
import com.carlos.imobiliaria.service.MunicipioService;

/*Author: Carlos Eduardo Marangoni Mendes


*/
@Controller
@RequestMapping("/bairros")
public class BairroController {


	@Autowired
	private BairroService bairroService;

	@Autowired
	private EstadoRepository estadoRepository;

	@Autowired
	private MunicipioRepository municipioRepository;

	@Autowired
	private MunicipioService municipioService;

	@GetMapping("/page/{pageNo}")
	public String findPaginated(@PathVariable("pageNo") int pageNo, Model model,
			@ModelAttribute("filtro") FilterDesc filtro, @RequestParam(defaultValue = "descricao") String radioFilter,
			RedirectAttributes attributes) {
		int pageSize = 3;// Número de registros por página

		Page<Bairro> page = null;

		try {
			page = bairroService.filtrarPage(pageNo, pageSize, filtro, radioFilter);
		} catch (NumberFormatException e) {
			attributes.addFlashAttribute("mensagemIntegridade", "Favor inserir um número na caixa de pesquisa.");
			return "redirect:/imoveis/page/1";
		}

		List<Bairro> listBairro = page.getContent();

		String filterParameters = bairroService.urlParams(filtro, radioFilter);

		model.addAttribute("currentPage", pageNo);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("bairros", listBairro);
		model.addAttribute("filterParameters", filterParameters);

		return "ListaBairros";

	}

	@GetMapping("/novo")
	public ModelAndView novo() {
		ModelAndView mv = new ModelAndView("CadastroBairro");
		mv.addObject(new Bairro()); // Manda o objeto para o form da view
		return mv;
	}

	@PostMapping
	public String salvar(@Validated Bairro bairro, Errors errors, Model model, RedirectAttributes attributes) {
		if (errors.hasErrors()) {
			return "CadastroBairro"; // Caso haja erro, não salva e retorna para mesma view
		}

		bairroService.salvar(bairro);
		attributes.addFlashAttribute("mensagem", "Bairro cadastrado com sucesso!");
		return "redirect:/bairros/novo";
	}

	@GetMapping("/{codigo}")
	public ModelAndView editar(@PathVariable("codigo") Bairro bairro) {
		ModelAndView mv = new ModelAndView("CadastroBairro");
		mv.addObject(bairro);
		return mv;
	}

	@DeleteMapping("/{codigo}")
	public String excluir(@PathVariable Long codigo, RedirectAttributes attributes) {

		try {
			bairroService.excluir(codigo);
		} catch (EntidadeEmUsoException e) { // Verifica se o bairro não está em uso por outra entidade.
			attributes.addFlashAttribute("mensagemIntegridade", e.getMessage());
			return "redirect:/bairros/page/1";
		}
		attributes.addFlashAttribute("mensagem", "Bairro excluído com sucesso!");
		return "redirect:/bairros/page/1";
	}

	@ModelAttribute("estados")
	public List<Estado> estados() {
		return estadoRepository.findAll();// Tornando o objeto disponível para as views

	}

	@ModelAttribute("municipios")
	public List<Municipio> municipios() {
		return municipioRepository.findAll();// Tornando o objeto disponível para as views

	}

	@GetMapping("/listarMunicipioPorEstado")
	@ResponseBody
	public List<Municipio> listarMunicipioPorEstado(Long codigoEstado) {

		List<Municipio> listaMunicipios = municipioService.encontrarMunicipiosPorEstado(codigoEstado); //Filtrando com base no select da VIEW CadastroBairro

		return listaMunicipios;
	}

}
