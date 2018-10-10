package com.tcc2.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONArray;

import br.com.starmetal.database.postgresql.InsertMaker;
import br.com.starmetal.database.postgresql.QueryMaker;
import br.com.starmetal.exceptions.DatabaseException;
import br.com.starmetal.results.ResultType;

public class Executor {
	
	private Connection connection;
	
	public Executor(Connection connection) {
		this.connection = connection;
	}
	
	/**
	 * Executa uma consulta na base de dados.
	 * @param query
	 * @param connection
	 * @return
	 */
	public JSONArray queryExecutor(final QueryMaker query) {
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
	 * Executa uma inserção na base de dados.
	 * @param insert
	 * @return
	 */
	public ResultType insertExecutor(final InsertMaker insert) {
		if(insert == null) {
			return ResultType.ERROR;
		}
		
		PreparedStatement statement = null;
		try {
			statement = this.connection.prepareStatement(insert.getInsert());
			statement.executeUpdate();
		} catch(SQLException sqle) {
			throw new DatabaseException("Problema ao executar insert na tabela 'relatorio_viagem'.", sqle);
		} finally {
			try {
				
				if(statement != null) statement.close();
				
			} catch(SQLException sqle) {
				throw new DatabaseException("Falha ao encerrar recursos de conexão com base de dados.", sqle.getMessage());
			}
		}
		
		return ResultType.SUCESS;
	}

}
