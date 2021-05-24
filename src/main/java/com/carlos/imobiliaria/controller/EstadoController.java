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
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.carlos.imobiliaria.exception.EntidadeEmUsoException;
import com.carlos.imobiliaria.filter.FilterDesc;
import com.carlos.imobiliaria.model.Estado;
import com.carlos.imobiliaria.service.EstadoService;

/*Author: Carlos Eduardo Marangoni Mendes


*/
@Controller
@RequestMapping("/estados")
public class EstadoController {



	@Autowired
	private EstadoService estadoService;

	@Autowired
	private RestTemplate restTemplate;

//	@Autowired
//    SenderMailService senderMailService;

	@GetMapping("/page/{pageNo}")
	public String findPaginated(@PathVariable("pageNo") int pageNo, Model model,
			@ModelAttribute("filtro") FilterDesc filtro, @RequestParam(defaultValue = "nome") String radioFilter,
			RedirectAttributes attributes) {
		int pageSize = 3;// Número de registros por página

		Page<Estado> page = estadoService.filtrarPage(pageNo, pageSize, filtro);

		List<Estado> listEstado = page.getContent(); // Tratar exceção de número do filtro de número de quartos vazio

		String filterParameters = estadoService.urlParams(filtro, radioFilter);

		model.addAttribute("currentPage", pageNo);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("estados", listEstado);
		model.addAttribute("filterParameters", filterParameters);

		return "ListaEstados";

	}

	@GetMapping("/novo")
	public ModelAndView novo() {
		ModelAndView mv = new ModelAndView("CadastroEstado");
		mv.addObject(new Estado());// Manda o objeto para o form da view

		return mv;
	}

	@PostMapping
	public String salvar(@Validated Estado estado, Errors errors, Model model, RedirectAttributes attributes) {

		if (errors.hasErrors()) {
			return "CadastroEstado"; // Caso haja algum erro, não salva e retorna para mesma view
		}

		estadoService.salvar(estado);
		attributes.addFlashAttribute("mensagem", "Estado cadastrado com sucesso!");
//		 senderMailService.enviar();
		return "redirect:/estados/novo";
	}

	@GetMapping("/{codigo}")
	public ModelAndView editar(@PathVariable("codigo") Estado estado) {
		ModelAndView mv = new ModelAndView("CadastroEstado");
		mv.addObject(estado);
		return mv;
	}

	@DeleteMapping("/{codigo}")
	public String excluir(@PathVariable Long codigo, RedirectAttributes attributes) {
		try {

			estadoService.excluir(codigo);
		} catch (EntidadeEmUsoException e) {
			attributes.addFlashAttribute("mensagemIntegridade", e.getMessage());
			return "redirect:/estados/page/1";
		}
		attributes.addFlashAttribute("mensagem", "Estado excluído com sucesso!");
		return "redirect:/estados/page/1";
	}

	@ModelAttribute("estadosAPI")
	public Estado[] estadosAPI() {
		String url = "https://servicodados.ibge.gov.br/api/v1/localidades/estados";// Consulta a API do IBGE com todos
																					// os
		return restTemplate.getForObject(url, Estado[].class); // estados do Brasil e os disponibiliza para a view

	}

}
