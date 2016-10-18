package br.com.visaocrcore.dao;

import java.util.List;

import br.com.visaocr.domain.DadosNota;
import br.com.visaocr.domain.Imagem;

public interface ImagemDao {
	 List<Imagem> listarImagensPendentes();
	 
	 void atualizarImagem(Imagem imagem, String jsonResultado, DadosNota dadosNota);
	 
	 void inserirImagemPendente(Imagem imagem);
	 
}
