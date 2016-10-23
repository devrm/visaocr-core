package br.com.visaocrcore.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import br.com.visaocr.domain.DadosNota;
import br.com.visaocr.domain.Imagem;

@Component
public class ImagemDaoJdbcImpl implements ImagemDao {

	
	private static Logger LOGGER = Logger.getLogger(ImagemDaoJdbcImpl.class);
	
	private final JdbcTemplate jdbcTemplate;

	@Autowired
	public ImagemDaoJdbcImpl(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}


	public List<Imagem> listarImagensPendentes() {

		Object [] params = {
			DadosNota.StatusNota.PENDENTE_OCR.getStatus()	
		};

		return this.jdbcTemplate.query("SELECT ID, CAMINHO_IMAGEM FROM TBL_DADOS_NOTA WHERE STATUS = ?", params,
				new ResultSetExtractor<List<Imagem>>() {

			@Override
			public List<Imagem> extractData(ResultSet rs) throws SQLException, DataAccessException {

				List<Imagem> imagens = new ArrayList<Imagem>();

				while (rs.next()) {
					Imagem imagem = new Imagem(rs.getString("CAMINHO_IMAGEM"));
					imagem.setId(rs.getInt("ID"));

					imagens.add(imagem);
				}
				return imagens;
			}
		});
	}


	@Override
	public void atualizarImagem(Imagem imagem, String resultadoAnalise, DadosNota dadosNota) {

		DateTimeFormatter formater = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		NumberFormat nf = NumberFormat.getInstance(new Locale("pt", "BR"));

		StringBuilder sql = new StringBuilder("UPDATE TBL_DADOS_NOTA SET ");
		sql.append("VALOR_NOTA = ?, ")
		   .append("COO = ?, ")
		   .append("CNPJ = ?, ")
		   .append("STATUS = ?, ")
		   .append("DATA_NOTA = ?, ")
		   .append("RESULTADO_ANALISE = ?, ")
		   .append("CAMINHO_IMAGEM_RESULTADO = ? ");
		sql.append("WHERE ID = ?");
		
		Number valor = 0L;
		LocalDate dataNota = null;
		
		try {
			TemporalAccessor data = formater.parse(dadosNota.getData());
			valor = nf.parse(dadosNota.getValorTotal());
			dataNota = LocalDate.from(data);
		} catch (ParseException e) {
			LOGGER.warn("Nao foi possivel formatar o valor: "+e.getMessage());
		} catch (DateTimeParseException e) {
			LOGGER.warn("Nao foi possivel formatar a data da nota: "+e.getMessage());
		}

		Object [] params = {
			valor,
			dadosNota.getCoo(),
			dadosNota.getCnpj(),
			dadosNota.getStatusNota().getStatus(),
			dataNota,
			resultadoAnalise,
			imagem.getCaminhoDestino(),
			imagem.getId()
		};

		this.jdbcTemplate.update(sql.toString(), params);
	}


	@Override
	public void inserirImagemPendente(Imagem imagem) {
		StringBuilder query = new StringBuilder("INSERT INTO TBL_DADOS_NOTA ");
		query.append("(CAMINHO_IMAGEM, STATUS) ")
		     .append("VALUES (?,?)");
		
		Object [] params = {
			imagem.getCaminho(),
			DadosNota.StatusNota.PENDENTE_OCR.getStatus()
		};
		
		this.jdbcTemplate.update(query.toString(), params);
		
	}



}
