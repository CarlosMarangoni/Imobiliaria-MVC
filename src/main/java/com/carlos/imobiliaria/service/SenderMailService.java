package com.carlos.imobiliaria.service;

import java.util.ArrayList;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.PathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.carlos.imobiliaria.model.Imovel;
import com.carlos.imobiliaria.model.User;
import com.carlos.imobiliaria.repository.UserRepository;

@Component
public class SenderMailService {

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private UserRepository userRepository;

	@Async
	public void enviar(Imovel imovel, MultipartFile multipartFile) throws MessagingException {
		List<User> usuarios = userRepository.findAll();
		List<String> emails = new ArrayList<>();
		usuarios.forEach(user -> emails.add(user.getEmail())); // Armazenando o email de todos os usuários cadastrados

		String[] address = emails.stream().toArray(String[]::new);
		MimeMessage message = mailSender.createMimeMessage();
		String mailSubject = "Novo imóvel - Imobija!";

		if (multipartFile.getOriginalFilename().equals("")) {// Caso o novo imóvel não possua uma imagem
			String mailContent = "<b>Olá, trazemos uma novidade!</b><p>Novo imóvel disponível na Imobija! Venha conferir diretamente no site da melhor imobiliária do Brasil! </p>";
			MimeMessageHelper helper = new MimeMessageHelper(message, true);

			helper.setFrom("testecarlosgft@gmail.com");// Remetente
			helper.setTo(address);// Destinatários

			helper.setSubject(mailSubject);
			helper.setText(mailContent, true);

		} else {
			String mailContent = "<b>Olá, trazemos uma novidade!</b><p>Novo imóvel disponível na Imobija! Venha conferir diretamente no site da melhor imobiliária do Brasil! </p>"
					+ "<p>Imagem:</p><hr><img src='cid:imovelImage' />";
			MimeMessageHelper helper = new MimeMessageHelper(message, true);

			helper.setFrom("testecarlosgft@gmail.com");// Remetente
			helper.setTo(address);// Destinatários

			helper.setSubject(mailSubject);
			helper.setText(mailContent, true);
			PathResource resource = new PathResource(
					"./imagens-imoveis/" + imovel.getCodigo() + "/" + imovel.getImagem());
			helper.addInline("imovelImage", resource);
		}

		mailSender.send(message);
	}
}
