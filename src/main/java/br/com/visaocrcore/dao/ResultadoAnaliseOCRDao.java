package br.com.visaocrcore.dao;

import java.util.List;

import br.com.visaocr.domain.DadosNota.StatusNota;
import br.com.visaocr.domain.ResultadoAnaliseOCR;

/**
 * 
 * 
 * @author rodmafra
 *
 */
public interface ResultadoAnaliseOCRDao {

	List<ResultadoAnaliseOCR> buscarDadosNotaPorStatus(StatusNota status);
	
}
