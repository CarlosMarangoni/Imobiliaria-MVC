package com.carlos.imobiliaria.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.carlos.imobiliaria.exception.EmailEmUsoException;
import com.carlos.imobiliaria.filter.FilterDesc;
import com.carlos.imobiliaria.model.User;
import com.carlos.imobiliaria.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	public void salvar(User user) {
		
		try {			
			userRepository.save(user);
		} catch (DataIntegrityViolationException e) {
			throw new EmailEmUsoException(String.format("E-mail já cadastrado."));
		}
	}

	public void excluir(Long codigo) {
		User user = userRepository.getOne(codigo);
		if(codigo != 1) {
			user.getRoles().clear();//Removendo Roles antes de excluir para não violar integridade
			userRepository.deleteById(codigo);
	
		}
		
	}
	public Page<User> filtrarPage(int pageNo, int pageSize, FilterDesc filtro, String radioFilter) {
		String descricao = filtro.getDescricao() == null ? "" : filtro.getDescricao();
		Pageable pageable = PageRequest.of(pageNo - 1, pageSize);

		switch (radioFilter) {		
		case "email":
			return userRepository.findByEmailContaining(descricao, pageable); //Verifica o parâmetro
		case "firstName":
			return userRepository.findByFirstNameContaining(descricao, pageable);																				// radioFilter na URL de
		case "lastName":
			return userRepository.findByLastNameContaining(descricao, pageable);																				// requisição e retorna o filtro
																						// conforme selecionado.
		default:
			return userRepository.findByEmailContaining(descricao, pageable);
		}

	}

	public String urlParams(FilterDesc filtro, String radioFilter) {
		String descricao = filtro.getDescricao() == null ? "" : filtro.getDescricao();
		return "descricao=" + descricao + "&radioFilter=" + radioFilter; // parâmetros da URL com os filtros para mandar
																			// para a VIEW, assim podendo mudar de
																			// página os filtros permanecerem
	}


}
	
	

