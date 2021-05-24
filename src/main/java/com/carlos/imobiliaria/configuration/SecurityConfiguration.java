	package com.carlos.imobiliaria.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.carlos.imobiliaria.service.CustomUserDetailsService;


@Configuration
@EnableWebSecurity

//@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	private static final String[] PRIVATE_GET = { "/users/**","/imoveis/novo", "/categorias/**", "/negocios/**", "/estados/**",
			"/municipios/**", "/bairros/**" };

	
	
	
	
	@Bean
	public UserDetailsService userDetailService() {
		return new CustomUserDetailsService();
	}
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailService());
		authProvider.setPasswordEncoder(passwordEncoder());
		
		return authProvider;
	}
	
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider());
	}

	@Override
	protected void configure(final HttpSecurity http) throws Exception {
		
		String[] resources = new String[] { "/", "/css/**", "/icons/**", "/img/**", "/js/**","/cadastrar" };
		
		
		http.cors().and().csrf().disable();//Desabilitando csrf
		http.authorizeRequests().antMatchers(resources).permitAll();//Permitindo a página home e os arquivos staticos para todos
		http.authorizeRequests()
			.antMatchers(HttpMethod.GET,PRIVATE_GET).hasAnyAuthority("ADMIN")
			.antMatchers(HttpMethod.POST).hasAnyAuthority("ADMIN")
			.antMatchers(HttpMethod.DELETE).hasAnyAuthority("ADMIN")
			.anyRequest()
			.authenticated()
			.and()
			.formLogin().loginPage("/entrar").permitAll().defaultSuccessUrl("/")
			.and().logout().logoutSuccessUrl("/").permitAll();
		
		
	} 
	

}


