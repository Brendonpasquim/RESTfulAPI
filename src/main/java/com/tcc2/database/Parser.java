package com.tcc2.database;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.json.JSONArray;
import org.json.JSONObject;

import com.tcc2.beans.PontosProximos;

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
	
    /**
     * Recebe um objeto ResultSet relacionado a uma consulta a pontos de ônibus e
     * preenche um JavaBean com os dados resultantes.
     * 
     * @param result [Obrigatório] - Objeto contendo uma tupla com dados de um ponto de ônibus.
     * @return [{@link com.tcc2.beans.PontosProximos}] contendo os dados da tupla do objeto ResultSet.
     * @throws SQLException
     */
    public static PontosProximos toPontoProximo(ResultSet result) throws SQLException{
    	if(result == null) {
    		return null;
    	}
    	
        PontosProximos pontoProximo = new PontosProximos();

        pontoProximo.setNumeroPonto (result.getInt	 ("numero_ponto"));
        pontoProximo.setEndereco    (result.getString("endereco"));
        pontoProximo.setTipo       	(result.getString("tipo"));
        pontoProximo.setCodigoLinha	(result.getInt   ("codigo_linha"));
        pontoProximo.setNomeLinha	(result.getString("nome_linha"));
        pontoProximo.setCor			(result.getString("cor"));
        pontoProximo.setApenasCartao(result.getString("apenas_cartao"));
        pontoProximo.setGeojson		(result.getString("geojson"));

        return pontoProximo;
    }
}
