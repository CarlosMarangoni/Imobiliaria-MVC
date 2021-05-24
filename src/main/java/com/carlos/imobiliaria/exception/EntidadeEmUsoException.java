package com.carlos.imobiliaria.exception;
/*Author: Carlos Eduardo Marangoni Mendes


*/
public class EntidadeEmUsoException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public EntidadeEmUsoException(String mensagem) {
		super(mensagem);
	}
}
