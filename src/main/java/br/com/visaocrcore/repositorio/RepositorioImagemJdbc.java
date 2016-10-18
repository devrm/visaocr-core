package br.com.visaocrcore.repositorio;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.visaocr.domain.DadosNota;
import br.com.visaocr.domain.Imagem;
import br.com.visaocrcore.dao.ImagemDao;

@Component
public class RepositorioImagemJdbc implements RepositorioImagem {

	@Autowired
	private ImagemDao imagemDao;

	@Override
	public List<Imagem> listarImagensPendentes() {
		return imagemDao.listarImagensPendentes();
	}

	@Override
	public void atualizarImagem(Imagem imagem, String jsonResultado, DadosNota dadosNota) {
		imagemDao.atualizarImagem(imagem, jsonResultado, dadosNota);
	}


}
