package com.tcc2.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

import com.tcc2.beans.PontosProximos;

import br.com.starmetal.database.postgresql.QueryMaker;
import br.com.starmetal.exceptions.DatabaseException;

public class DAOPontos {

    private Connection connection;

    public DAOPontos(Connection connection){
        this.connection = connection;
    }

	/**
	 * Executa a query e mantém a conexão disponível para uso.
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
     * A partir de uma coordenada latitude e longitude determina Pontos de ônibus próximos,
     * considerando um raio de abrangência arbitrário.
     * 
     * @param latitude [Obrigatório] - Uma coordenada latitude.
     * @param longitude [Obrigatório] - Uma coordenada longitude.
     * @return [java.util.List] contendo uma lista de pontos de ônibus próximos.
     */
    public List<PontosProximos> consultarPontosDeOnibusProximos(double latitude, double longitude){
    	QueryMaker query = new QueryMaker();
    	query.select("DISTINCT P.numero_ponto", "P.endereco", "P.tipo", "P.codigo_linha", "L.nome_linha", "L.cor", "L.apenas_cartao", "ST_AsGeoJSON(P.geom, 15, 0) as geojson")
    		.from("pontos_de_onibus P, linhas_de_onibus L")
    		.where("ST_Within(geom, ST_buffer(ST_GeomFromText('POINT( :longitude :latitude )', 4326), 0.0025))")
    		.where("P.codigo_linha = L.codigo_linha")
    		.setParameter("longitude", longitude)
    		.setParameter("latitude", latitude);
    	
    	List<PontosProximos> listaDePontos = new ArrayList<>();
    	try {
        	PreparedStatement statement = connection.prepareStatement(query.getQuery());
        	
        	ResultSet result = statement.executeQuery();
        	while(result.next()) {
        		PontosProximos pontoDeOnibus = Parser.toPontoProximo(result);
        		listaDePontos.add(pontoDeOnibus);
        	}
        	
        	result.close();
        	statement.close();
        	
    	} catch(SQLException sqle) {
    		throw new DatabaseException("Falha ao executar consulta por Pontos de Ônibus próximos", sqle.getMessage());
    	}
    	
    	return listaDePontos;
    }
    
    public JSONArray consultarPontosDeOnibusProximosSimplificado(double latitude, double longitude) {
    	QueryMaker query = new QueryMaker();
    	query.select("numero_ponto")
    		 .from("pontos_de_onibus")
    		 .where("ST_Within(geom, ST_buffer(ST_GeomFromText('POINT(lon_origem lat_origem)', 4326), 0.0025))");
    	
    	return QueryExecutor(query);
    }
    
    /**
     * Consulta todos os pontos de ônibus disponíveis.
     * 
     * @return [org.json.JSONArray] contendo uma lista de pontos de ônibus
     */
    public JSONArray consultarPontosDeOnibus() {
    	QueryMaker query = new QueryMaker();
        query.select("P.numero_ponto", "P.endereco", "P.tipo", "P.codigo_linha", "L.nome_linha", "P.seq", "L.apenas_cartao", "ST_AsGeoJSON(P.geom, 15, 0) as geojson")
        	 .from("pontos_de_onibus P, linhas_de_onibus L")
        	 .where("P.codigo_linha = L.codigo_linha")
        	 .orderBy("P.numero_ponto");
        
        return QueryExecutor(query);
    }

    /**
     * 
     * @return
     */
    public JSONArray consultarTiposDePonto() {
    	QueryMaker query = new QueryMaker();
    	query.select("DISTINCT tipo")
    		 .from("pontos_de_onibus")
    		 .orderBy("tipo");
    	
    	return QueryExecutor(query);
    }
    
    /**
     * 
     * @return
     */
    public JSONArray consultarBairros() {
    	QueryMaker query = new QueryMaker();
    	query.select("DISTINCT nome")
    		 .from("divisa_de_bairros")
    		 .orderBy("nome");
    	
    	return QueryExecutor(query);
    }
    
    /**
     * 
     * @return
     */
    public JSONArray consultarLinhas() {
    	QueryMaker query = new QueryMaker();
    	query.select("DISTINCT codigo_linha", "nome_linha")
    		 .from("linhas_de_onibus ")
    		 .orderBy("codigo_linha");
    	
    	return QueryExecutor(query);
    }
    
    /**
     * 
     * @return
     */
    public JSONArray consultarCategoriasDeLinhas() {
    	QueryMaker query = new QueryMaker();
    	query.select("DISTINCT categoria")
    		 .from("linhas_de_onibus  ")
    		 .orderBy("categoria");
    	
    	return QueryExecutor(query);
    }
    
    /**
     * 
     * @return
     */
    public JSONArray consultarItinerarios() {
    	QueryMaker clausuraWith = new QueryMaker(); 
    	clausuraWith.select("codigo_linha", "MAX(shape_len) AS tamanho")
    				.from("itinerarios_de_onibus")
    				.groupBy("codigo_linha");
    	
    	QueryMaker query = new QueryMaker();
    	query.with(clausuraWith, "tabela_aux")
    		 .select("nome_linha", "codigo_linha", "nome_categoria", "ST_AsGeoJSON(ST_Transform(ST_SetSRID(geom, 29192), 4326), 15, 0) as geojson")
			 .from("itinerarios_de_onibus ")
		     .where("shape_len", "(SELECT tabela_aux.tamanho FROM tabela_aux WHERE tabela_aux.codigo_linha = itinerarios_de_onibus.codigo_linha)")
		     .orderBy("nome_linha");
    	
    	return QueryExecutor(query);
    }
    
    /**
     * 
     * @return
     */
    public JSONArray consultarHorarios() {
    	QueryMaker query = new QueryMaker();
    	query.select("codigo_linha", "horario_saida", "ponto", "tipo_dia", "numero_ponto", "adaptado")
    		 .from("horarios_de_onibus")
    		 .orderBy("codigo_linha", "ponto", "tipo_dia", "horario_saida");
    	
    	return QueryExecutor(query);
    }
    
    /**
     * 
     * @return
     */
    public JSONArray consultarProblemasOnibusCrowdsourcing() {
    	QueryMaker query = new QueryMaker();
    	query.select("C.dia", "C.horario", "C.tipo", "C.numero_ponto", "P.tipo", "P.endereco", "ST_AsGeoJSON(P.geom, 15, 0) as geojson")
    		 .from("crowdsourcing_pontos C, divisa_de_bairros B, pontos_de_onibus P")
    		 .where("ST_Within(P.geom, ST_Transform(ST_setSRID(B.geom, 29192), 4326))")
    		 .where("C.numero_ponto = P.numero_ponto");
    	
    	return QueryExecutor(query);
    }
    
    /**
     * 
     * @return
     */
    public JSONArray consultarProblemasLinhasCrowdsourcing() {
    	QueryMaker query = new QueryMaker();
    	query.select("C.dia, C.horario, B.nome, C.tipo, C.codigo_linha, L.nome_linha, L.cor, L.categoria, L.apenas_cartao, ST_AsGeoJSON(ST_SetSRID(ST_MakePoint(C.lon, C.lat),4326), 15, 0) as geojson")
    		 .from("crowdsourcing_linhas C, linhas_de_onibus L, divisa_de_bairros B")
    		 .where("ST_Within(ST_SetSRID(ST_MakePoint(C.lon, C.lat),4326), ST_Transform(ST_setSRID(B.geom, 29192), 4326))")
    		 .where("L.codigo_linha = C.codigo_linha");
    	
    	return QueryExecutor(query);
    }
}
