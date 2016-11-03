package br.com.visaocrcore.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import br.com.visaocr.domain.DadosNota;
import br.com.visaocr.domain.DadosNota.StatusNota;
import br.com.visaocr.domain.Imagem;
import br.com.visaocr.domain.ResultadoAnaliseOCR;

@Repository
public class ResultadoAnaliseOCRDaoJdbcImpl implements ResultadoAnaliseOCRDao {

	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	public ResultadoAnaliseOCRDaoJdbcImpl(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	@Override
	public List<ResultadoAnaliseOCR> buscarDadosNotaPorStatus(StatusNota status) {

		StringBuilder query = new StringBuilder("SELECT DATA_NOTA, COO, VALOR_NOTA, CNPJ, STATUS, CAMINHO_IMAGEM_RESULTADO ");
		query.append("FROM TBL_DADOS_NOTA WHERE STATUS = ?");
		
		Object [] params = {
			status.getStatus()
		};
		
		return this.jdbcTemplate.query(query.toString(), params, new ResultadoAnaliseOCRMapper());
	}

	class ResultadoAnaliseOCRMapper implements ResultSetExtractor<List<ResultadoAnaliseOCR>> {

		@Override
		public List<ResultadoAnaliseOCR> extractData(ResultSet rs) throws SQLException, DataAccessException {
			List<ResultadoAnaliseOCR> resultados = new ArrayList<ResultadoAnaliseOCR>();
			
			while (rs.next()) {
				DadosNota dadosNota = new DadosNota();
				dadosNota.setCnpj(rs.getString("CNPJ"));
				dadosNota.setCoo(rs.getString("COO"));
				SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
				Date dataNota = rs.getDate("DATA_NOTA");
				
				if (dataNota != null) {
					dadosNota.setData(format.format(dataNota));
				}
				
				dadosNota.setValorTotal(String.valueOf(rs.getDouble("VALOR_NOTA")));
				Imagem imagem = new Imagem(rs.getString("CAMINHO_IMAGEM_RESULTADO"));
				
				ResultadoAnaliseOCR resultado = new ResultadoAnaliseOCR();
				resultado.setDadosNota(dadosNota);
				resultado.setImagem(imagem);
				resultados.add(resultado);
			}
			
			return resultados;
		}
		
	}
	
	
}
