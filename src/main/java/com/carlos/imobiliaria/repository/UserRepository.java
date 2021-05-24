package com.carlos.imobiliaria.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.carlos.imobiliaria.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
	@Query("SELECT u FROM User u WHERE u.email = ?1")
	User findByEmail(String email);

	Page<User> findByEmailContaining(String descricao, Pageable pageable);

	Page<User> findByFirstNameContaining(String descricao, Pageable pageable);

	Page<User> findByLastNameContaining(String descricao, Pageable pageable);

	
	
	
}
