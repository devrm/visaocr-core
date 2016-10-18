package br.com.visaocrcore.validador;

import org.apache.commons.lang3.StringUtils;

import br.com.visaocr.domain.DadosNota;

public class VerificadorDadosNota {

	private DadosNota dadosNota;

	public VerificadorDadosNota(DadosNota dadosNota) {
		this.dadosNota = dadosNota;
	}

	public boolean isDadosNotaValidos() {
		boolean isDadosValidos = true;
		
		if (StringUtils.isBlank(dadosNota.getCnpj()) ||
			StringUtils.isBlank(dadosNota.getCoo()) ||
			StringUtils.isBlank(dadosNota.getValorTotal()) ||
			StringUtils.isBlank(dadosNota.getData()) ) {
			
			isDadosValidos = false;
			
		}
		return isDadosValidos;
	}
	
	
}
