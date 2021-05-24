package com.carlos.imobiliaria.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

import com.carlos.imobiliaria.exception.EmailEmUsoException;
import com.carlos.imobiliaria.filter.FilterDesc;
import com.carlos.imobiliaria.model.Role;
import com.carlos.imobiliaria.model.User;
import com.carlos.imobiliaria.repository.RoleRepository;
import com.carlos.imobiliaria.service.UserService;

@Controller
@RequestMapping("/users")
public class UserController {
		

	
	@Autowired
	private UserService userService;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@GetMapping("/page/{pageNo}")
	public String findPaginated(@PathVariable("pageNo") int pageNo,Model model,
			@ModelAttribute("filtro") FilterDesc filtro, @RequestParam(defaultValue = "email") String radioFilter,
			RedirectAttributes attributes) {
		
		int pageSize = 5;// Número de registros por página
		
		Page<User> page = null;

		try {
			page = userService.filtrarPage(pageNo, pageSize, filtro, radioFilter);
		} catch (NumberFormatException e) {
			attributes.addFlashAttribute("mensagemIntegridade", "Favor inserir um número na caixa de pesquisa.");
			return "redirect:/users/page/1";
		}

		List<User> usuarios = page.getContent(); // Tratar exceção de número do filtro de número de quartos
															// vazio
		String filterParameters = userService.urlParams(filtro, radioFilter);
		model.addAttribute("currentPage", pageNo);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("usuarios", usuarios);
		model.addAttribute("filterParameters", filterParameters);
		return "ListaUsuarios";
	}
	
	@GetMapping("/novo")
	public String novo(Model model) {
		List<Role> listRole = roleRepository.findAll();
		model.addAttribute("listRole", listRole);
		model.addAttribute(new User());
		return "CadastroUsuario";
	}
	
	@PostMapping
	public String novo(@Validated User usuario, Errors errors, Model model, RedirectAttributes attributes) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String encodedPassword = encoder.encode(usuario.getPassword());
		usuario.setPassword(encodedPassword);	
		try {
			
			userService.salvar(usuario);
		} catch (EmailEmUsoException e) {
			attributes.addFlashAttribute("mensagemIntegridade", e.getMessage());
			return "redirect:/users/novo";
		}
		attributes.addFlashAttribute("mensagem", "Usuário cadastrado com sucesso!");
		return "redirect:/users/novo";
	}
	
	@GetMapping("/{codigo}")
	public ModelAndView editar(@PathVariable("codigo") User usuario,Model model) {
		List<Role> listRole = roleRepository.findAll();
		model.addAttribute("listRole", listRole);
		ModelAndView mv = new ModelAndView("CadastroUsuario");
		mv.addObject(usuario);
		return mv;
	}
	
	@DeleteMapping("/{codigo}")
	public String excluir(@PathVariable Long codigo, RedirectAttributes attributes) {
		userService.excluir(codigo);		
		attributes.addFlashAttribute("mensagem", "Usuário excluído com sucesso!");
		return "redirect:/users/page/1";
	}
	

}
