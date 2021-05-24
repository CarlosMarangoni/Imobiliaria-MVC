package com.carlos.imobiliaria.exception;
/*Author: Carlos Eduardo Marangoni Mendes


*/
public class EmailEmUsoException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public EmailEmUsoException(String mensagem) {
		super(mensagem);
	}
}
