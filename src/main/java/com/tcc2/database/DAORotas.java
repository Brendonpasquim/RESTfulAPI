package com.tcc2.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONArray;

import br.com.starmetal.database.postgresql.QueryMaker;
import br.com.starmetal.exceptions.DatabaseException;

public class DAORotas {

	private Connection connection;
	
	public DAORotas(Connection connection) {
		this.connection = connection;
	}
	
	/**
	 * Executa a query
	 * @param query
	 * @param connection
	 * @return
	 */
	private JSONArray QueryExecutor(QueryMaker query) {
		if(query == null) {
			return new JSONArray();
		}
		
		PreparedStatement statement = null;
		ResultSet result = null;
		JSONArray jsonArray;
		try {
			statement = connection.prepareStatement(query.getQuery());
            result = statement.executeQuery();
			jsonArray = Parser.toJSON(result);
			
		} catch(SQLException sqle) {
			throw new DatabaseException("Falha ao executar consulta na base de Dados da UTFPR.", sqle.getMessage());
		} finally {
			try{
				
				if(statement 	!= null) statement.close();
				if(result 		!= null) result.close();
				
			} catch(SQLException sqle) {
				throw new DatabaseException("Falha ao encerrar recursos de conexão com base de dados.", sqle.getMessage());
			}
		}
		
		return jsonArray;
	}
	
	/**
	 * 
	 * @return
	 */
	public JSONArray consultarOrigem() {
		QueryMaker query = new QueryMaker();
		query.select("R.data_viagem", "R.horario_saida", "R.codigo_linha", "L.nome_linha", "L.cor", "L.apenas_cartao", "R.ponto_saida", "P.endereco", "P.tipo, ST_AsGeoJSON(P.geom, 15, 0) as geojson")
			 .from("relatorio_viagem R, pontos_de_onibus P, linhas_de_onibus L, divisa_de_bairros B")
			 .where("ST_Within(P.geom, ST_Transform(ST_setSRID(B.geom, 29192), 4326))")
			 .where("R.codigo_linha = L.codigo_linha")
			 .where("R.numero_ponto = P.numero_ponto");
		
		return QueryExecutor(query);
	}
	
	/**
	 * 
	 * @return
	 */
	public JSONArray consultarDestino() {
		QueryMaker query = new QueryMaker();
		query.select("R.data_viagem", "R.horario_chegada", "R.codigo_linha", "L.nome_linha", "L.cor", "L.apenas_cartao", "R.ponto_chegada", "P.endereco", "P.tipo", "ST_AsGeoJSON(P.geom, 15, 0) as geojson")
			 .from("relatorio_viagem R, pontos_de_onibus P, linhas_de_onibus L, divisa_de_bairros B")
			 .where("ST_Within(P.geom, ST_Transform(ST_setSRID(B.geom, 29192), 4326))")
			 .where("R.codigo_linha = L.codigo_linha")
			 .where("R.numero_ponto = P.numero_ponto");
		
		return QueryExecutor(query);
	}
}
