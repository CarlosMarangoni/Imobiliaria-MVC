package com.carlos.imobiliaria.configuration;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvc implements WebMvcConfigurer {


	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {

		Path imageUploadDir = Paths.get("./imagens-imoveis");
		String imageUploadPath = imageUploadDir.toFile().getAbsolutePath();
		registry.addResourceHandler("/imagens-imoveis/**").addResourceLocations("file:/" + imageUploadPath + "/"); //Adicionando imagens como recursos estáticos
	}

}
