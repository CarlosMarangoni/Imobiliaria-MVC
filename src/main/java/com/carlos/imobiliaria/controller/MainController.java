package com.carlos.imobiliaria.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.carlos.imobiliaria.exception.EmailEmUsoException;
import com.carlos.imobiliaria.model.Role;
import com.carlos.imobiliaria.model.User;
import com.carlos.imobiliaria.repository.RoleRepository;
import com.carlos.imobiliaria.service.UserService;

@Controller
public class MainController {

	private final static Long ID_ROLE_USER = 2L;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private UserService userService;

	@GetMapping("")
	public String novo() {
		return "index";
	}

	@GetMapping("/entrar")
	public String viewEntrar() {
		return "entrar";
	}

	@GetMapping("/cadastrar")
	public String viewCadastrar() {
		return "Cadastrar";
	}

	@PostMapping("/cadastrar")
	public String cadastrar(User usuario, RedirectAttributes attributes) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String encodedPassword = encoder.encode(usuario.getPassword());
		Role role = roleRepository.getOne(ID_ROLE_USER);
		usuario.setPassword(encodedPassword);
		usuario.addRole(role);
		try {

			userService.salvar(usuario);
		} catch (EmailEmUsoException e) {
			attributes.addFlashAttribute("mensagemIntegridade", e.getMessage());
			return "redirect:/cadastrar";
		}
			
		attributes.addFlashAttribute("mensagem", "Usuário cadastrado com sucesso!");
		return "redirect:/cadastrar";

	}
}
