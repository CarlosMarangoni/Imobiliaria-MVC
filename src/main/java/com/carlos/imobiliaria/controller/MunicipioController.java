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
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.carlos.imobiliaria.exception.EntidadeEmUsoException;
import com.carlos.imobiliaria.filter.FilterDesc;
import com.carlos.imobiliaria.model.Estado;
import com.carlos.imobiliaria.model.Municipio;
import com.carlos.imobiliaria.repository.EstadoRepository;
import com.carlos.imobiliaria.service.MunicipioService;

/*Author: Carlos Eduardo Marangoni Mendes


*/
@Controller
@RequestMapping("/municipios")
public class MunicipioController {


	@Autowired
	private MunicipioService municipioService;

	@Autowired
	private EstadoRepository estadoRepository;

	@GetMapping("/page/{pageNo}")
	public String findPaginated(@PathVariable("pageNo") int pageNo, Model model,
			@ModelAttribute("filtro") FilterDesc filtro, @RequestParam(defaultValue = "descricao") String radioFilter,
			RedirectAttributes attributes) {
		int pageSize = 3;// Número de registros por página

		Page<Municipio> page = null;

		try {
			page = municipioService.filtrarPage(pageNo, pageSize, filtro, radioFilter);
		} catch (NumberFormatException e) {
			attributes.addFlashAttribute("mensagemIntegridade", "Favor inserir um número na caixa de pesquisa.");
			return "redirect:/imoveis/page/1";
		}

		List<Municipio> listMunicipio = page.getContent(); 

		String filterParameters = municipioService.urlParams(filtro, radioFilter);

		model.addAttribute("currentPage", pageNo);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("municipios", listMunicipio);
		model.addAttribute("filterParameters", filterParameters);

		return "ListaMunicipios";

	}

	@GetMapping("/novo")
	public ModelAndView novo() {
		ModelAndView mv = new ModelAndView("CadastroMunicipio");
		mv.addObject(new Municipio());// Manda o objeto para o form da view
		return mv;
	}

	@PostMapping
	public String salvar(@Validated Municipio municipio, Errors errors, Model model, RedirectAttributes attributes) {
		if (errors.hasErrors()) {
			return "CadastroMunicipio";// Caso haja algum erro, não salva e retorna para mesma view
		}

		municipioService.salvar(municipio);
		attributes.addFlashAttribute("mensagem", "Municipio cadastrado com sucesso!");
		return "redirect:/municipios/novo";
	}

	@GetMapping("/{codigo}")
	public ModelAndView editar(@PathVariable("codigo") Municipio municipio) {
		ModelAndView mv = new ModelAndView("CadastroMunicipio");
		mv.addObject(municipio);
		return mv;
	}

	@DeleteMapping("/{codigo}")
	public String excluir(@PathVariable Long codigo, RedirectAttributes attributes) {

		try {
			municipioService.excluir(codigo);

		} catch (EntidadeEmUsoException e) {// Verifica se o município não está em uso por outra entidade.
			attributes.addFlashAttribute("mensagemIntegridade", e.getMessage());
			return "redirect:/municipios/page/1";
		}
		attributes.addFlashAttribute("mensagem", "Municipio excluído com sucesso!");
		return "redirect:/municipios/page/1";
	}

	@ModelAttribute("estados")
	public List<Estado> estados() {
		return estadoRepository.findAll();// Tornando o objeto disponível para as views

	}

}
