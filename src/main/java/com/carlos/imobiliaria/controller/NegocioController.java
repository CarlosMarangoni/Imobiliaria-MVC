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
import com.carlos.imobiliaria.model.Negocio;
import com.carlos.imobiliaria.service.NegocioService;

/*Author: Carlos Eduardo Marangoni Mendes


*/
@Controller
@RequestMapping("/negocios")
public class NegocioController {



	@Autowired
	private NegocioService negocioService;

	@GetMapping("/page/{pageNo}")
	public String findPaginated(@PathVariable("pageNo") int pageNo, Model model,
			@ModelAttribute("filtro") FilterDesc filtro, @RequestParam(defaultValue = "nome") String radioFilter,
			RedirectAttributes attributes) {
		int pageSize = 3;// Número de registros por página

		Page<Negocio> page = negocioService.filtrarPage(pageNo, pageSize, filtro);

		List<Negocio> listNegocio = page.getContent(); // Tratar exceção de número do filtro de número de quartos vazio

		String filterParameters = negocioService.urlParams(filtro, radioFilter);

		model.addAttribute("currentPage", pageNo);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("negocios", listNegocio);
		model.addAttribute("filterParameters", filterParameters);

		return "ListaNegocios";

	}

	@GetMapping("/novo")
	public ModelAndView novo() {
		ModelAndView mv = new ModelAndView("CadastroNegocio");
		mv.addObject(new Negocio());// Manda o objeto para o form da view
		return mv;
	}

	@PostMapping
	public String salvar(@Validated Negocio negocio, Errors errors, RedirectAttributes attributes) {

		if (errors.hasErrors()) {
			return "CadastroNegocio";// Caso haja algum erro na validação dos campos, não salva e retorna para mesma
										// view
		}

		negocioService.salvar(negocio);
		attributes.addFlashAttribute("mensagem", "Negocio cadastrado com sucesso!");
		return "redirect:/negocios/novo";
	}

	@GetMapping("/{codigo}")
	public ModelAndView editar(@PathVariable("codigo") Negocio negocio) {
		ModelAndView mv = new ModelAndView("CadastroNegocio");
		mv.addObject(negocio);
		return mv;
	}

	@DeleteMapping("/{codigo}")
	public String excluir(@PathVariable Long codigo, RedirectAttributes attributes) {

		try {

			negocioService.excluir(codigo);
		} catch (EntidadeEmUsoException e) {// Verifica se o negocio não está em uso por outra entidade.
			attributes.addFlashAttribute("mensagemIntegridade", e.getMessage());
			return "redirect:/negocios/page/1";
		}
		attributes.addFlashAttribute("mensagem", "Negocio excluído com sucesso!");
		return "redirect:/negocios/page/1";
	}
}
