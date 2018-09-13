package com.tcc2.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONArray;

import br.com.starmetal.database.postgresql.QueryMaker;
import br.com.starmetal.exceptions.DatabaseException;

public class QueryExecutor {
	
	private Connection connection;
	
	public QueryExecutor(Connection connection) {
		this.connection = connection;
	}
	
	/**
	 * Executa a query
	 * @param query
	 * @param connection
	 * @return
	 */
	public JSONArray QueryExecutor(QueryMaker query) {
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

}
