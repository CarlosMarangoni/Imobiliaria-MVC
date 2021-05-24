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
import com.carlos.imobiliaria.model.Categoria;
import com.carlos.imobiliaria.service.CategoriaService;

/*Author: Carlos Eduardo Marangoni Mendes


*/
@Controller
@RequestMapping("/categorias")
public class CategoriaController {


	@Autowired
	private CategoriaService categoriaService;

	@GetMapping("/page/{pageNo}")
	public String findPaginated(@PathVariable("pageNo") int pageNo, Model model,
			@ModelAttribute("filtro") FilterDesc filtro, @RequestParam(defaultValue = "nome") String radioFilter,
			RedirectAttributes attributes) {
		int pageSize = 3;// Número de registros por página

		Page<Categoria> page = categoriaService.filtrarPage(pageNo, pageSize, filtro);

		List<Categoria> listCategoria = page.getContent(); // Tratar exceção de número do filtro de número de quartos
															// vazio

		String filterParameters = categoriaService.urlParams(filtro, radioFilter);

		model.addAttribute("currentPage", pageNo);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("categorias", listCategoria);
		model.addAttribute("filterParameters", filterParameters);

		return "ListaCategorias";

	}

	@GetMapping("/novo")
	public ModelAndView novo() {
		ModelAndView mv = new ModelAndView("CadastroCategoria");
		mv.addObject(new Categoria());// Manda o objeto para o form da view
		return mv;
	}

	@PostMapping
	public String salvar(@Validated Categoria categoria, Errors errors, RedirectAttributes attributes) {

		if (errors.hasErrors()) {
			return "CadastroCategoria";// Caso haja algum erro, não salva e retorna para mesma view
		}

		categoriaService.salvar(categoria);
		attributes.addFlashAttribute("mensagem", "Categoria cadastrada com sucesso!");
		return "redirect:/categorias/novo";
	}

	@GetMapping("/{codigo}")
	public ModelAndView editar(@PathVariable("codigo") Categoria categoria) {
		ModelAndView mv = new ModelAndView("CadastroCategoria");
		mv.addObject(categoria);
		return mv;
	}

	@DeleteMapping("/{codigo}")
	public String excluir(@PathVariable Long codigo, RedirectAttributes attributes) {

		try {
			categoriaService.excluir(codigo);
		} catch (EntidadeEmUsoException e) {// Verifica se o bairro não está em uso por outra entidade.
			attributes.addFlashAttribute("mensagemIntegridade", e.getMessage());
			return "redirect:/categorias/page/1";
		}
		attributes.addFlashAttribute("mensagem", "Categoria excluída com sucesso!");
		return "redirect:/categorias/page/1";
	}
}
