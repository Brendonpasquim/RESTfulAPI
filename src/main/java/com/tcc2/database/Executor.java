package com.tcc2.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONObject;

import com.tcc2.beans.PontosProximos;

import br.com.starmetal.database.postgresql.InsertMaker;
import br.com.starmetal.database.postgresql.QueryMaker;
import br.com.starmetal.exceptions.DatabaseException;
import br.com.starmetal.results.ResultType;

public class Executor {

	private static final Logger LOG = Logger.getLogger(Executor.class.getName());
	private final Connection connection;

	public Executor(Connection connection) {
		this.connection = connection;
	}

	/**
	 * Executa uma consulta na base de dados.
	 * 
	 * @param query
	 * @return
	 */
	public JSONArray queryExecutor(final QueryMaker query) {
		if (query == null) {
			return new JSONArray();
		}
		
		JSONArray jsonArray;
		String queryString = query.getQuery();
		try(PreparedStatement statement = connection.prepareStatement(queryString)){
			try(ResultSet result = statement.executeQuery()) {
				LOG.info("Status Query: [EXECUTANDO]");
				LOG.info(queryString);
				jsonArray = Executor.Parser.toJSON(result);
				LOG.info("Status Query: [FINALIZADA]");
			}
		} catch (SQLException sqle) {
			throw new DatabaseException("Falha ao executar consulta na base de Dados da UTFPR.", sqle.getMessage());
		}

		return jsonArray;
	}	
	
	/**
	 * Executa uma inserção na base de dados.
	 * 
	 * @param insert
	 * @return
	 */
	public ResultType insertExecutor(final InsertMaker insert) {
		if (insert == null) {
			return ResultType.ERROR;
		}

		try(PreparedStatement statement = this.connection.prepareStatement(insert.getInsert())){
			statement.executeUpdate();
		} catch (SQLException sqle) {
			throw new DatabaseException("Problema ao executar insert na tabela 'relatorio_viagem'.", sqle);
		} 
		
		return ResultType.SUCESS;
	}
	
	
	public static class Parser {

		/**
		 * 
		 * @param result
		 * @return
		 * @throws SQLException
		 */
		public static JSONArray toJSON(ResultSet result) throws SQLException {
			if (result == null) {
				return null;
			}

			ResultSetMetaData metadados = result.getMetaData();
			int numeroDeColunas = metadados.getColumnCount();
			JSONArray jsonArray = new JSONArray();
			JSONObject json;

			while (result.next()) {

				json = new JSONObject();
				for (int indice = 1; indice <= numeroDeColunas; indice++) {
					String nomeColuna = metadados.getColumnName(indice);
					json.put(nomeColuna, result.getObject(nomeColuna));
				}

				jsonArray.put(json);
			}

			return jsonArray;
		}

		/**
		 * Recebe um objeto ResultSet relacionado a uma consulta a pontos de ônibus e
		 * preenche um JavaBean com os dados resultantes.
		 * 
		 * @param result
		 *            [Obrigatório] - Objeto contendo uma tupla com dados de um ponto de
		 *            ônibus.
		 * @return [{@link com.tcc2.beans.PontosProximos}] contendo os dados da tupla do
		 *         objeto ResultSet.
		 * @throws SQLException
		 */
		public static PontosProximos toPontoProximo(ResultSet result) throws SQLException {
			if (result == null) {
				return null;
			}

			PontosProximos pontoProximo = new PontosProximos();

			pontoProximo.setNumeroPonto(result.getInt("numero_ponto"));
			pontoProximo.setEndereco(	result.getString("endereco"));
			pontoProximo.setTipo(		result.getString("tipo"));
			pontoProximo.setCodigoLinha(result.getString("codigo_linha"));
			pontoProximo.setNomeLinha(	result.getString("nome_linha"));
			pontoProximo.setCor(		result.getString("cor"));
			pontoProximo.setApenasCartao(result.getString("apenas_cartao"));
			pontoProximo.setGeojson(	result.getString("geojson"));

			return pontoProximo;
		}
	}
}
