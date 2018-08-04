package com.tcc2.database;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.json.JSONArray;
import org.json.JSONObject;

public class Parser {
	
	/**
	 * 
	 * @param result
	 * @return
	 * @throws SQLException
	 */
	public static JSONArray toJSON(ResultSet result) throws SQLException {
		if(result == null) {
			return null;
		}
		
		ResultSetMetaData metadados = result.getMetaData();
		int numeroDeColunas = metadados.getColumnCount();
		JSONArray jsonArray = new JSONArray();
		JSONObject json;
		
		while(result.next()) {
			
			json = new JSONObject();
			for(int indice = 1; indice <= numeroDeColunas; indice++) {
				String nomeColuna = metadados.getColumnName(indice);
				json.put(nomeColuna, result.getObject(nomeColuna));
			}
			
			jsonArray.put(json);
		}
		
		return jsonArray;
	}
	


}
