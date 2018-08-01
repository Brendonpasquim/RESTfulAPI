package com.tcc2.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.tcc2.beans.PontoDeOnibus;

import br.com.starmetal.database.QueryMaker;
import br.com.starmetal.exceptions.DatabaseException;

public class DAOPontos {

    private static Connection connection;

    public DAOPontos(){
        connection = DAOBaseUTFPR.factory.getConnectionWithSSH();
    }

    /**
     * A partir de uma coordenada latitude e longitude determina Pontos de ônibus próximos,
     * considerando um raio de abrangência arbitrário.
     * 
     * @param latitude [Obrigatório] - Uma coordenada latitude.
     * @param longitude [Obrigatório] - Uma coordenada longitude.
     * @return [java.util.List] contendo uma lista de pontos de ônibus próximos.
     */
    public List<PontoDeOnibus> consultarPontosDeOnibusProximos(double latitude, double longitude){
    	
    	List<PontoDeOnibus> listaDePontos = new ArrayList<>();
    	
    	QueryMaker query = new QueryMaker();
    	query.select("*")
    		.from("public.pontos_de_onibus")
    		.where("ST_Within(geom, ST_buffer(ST_GeomFromText('POINT( :longitude :latitude )', 4326), 0.0025))")
    		.setParameter("longitude", longitude)
    		.setParameter("latitude", latitude);
    	
    	try {
        	PreparedStatement statement = connection.prepareStatement(query.getQuery());
        	
        	ResultSet result = statement.executeQuery();
        	while(result.next()) {
        		PontoDeOnibus pontoDeOnibus = getPontoDeOnibus(result);
        		listaDePontos.add(pontoDeOnibus);
        	}
        	
    	} catch(SQLException sqle) {
    		throw new DatabaseException("Falha ao executar consulta por Pontos de Ônibus próximos", sqle.getMessage());
    	}
    	
    	return listaDePontos;
    }
    
    /**
     * Recebe um objeto ResultSet relacionado a uma consulta a pontos de ônibus e
     * preenche um JavaBean com os dados resultantes.
     * 
     * @param result [Obrigatório] - Objeto contendo uma tupla com dados de um ponto de ônibus.
     * @return [{@link com.tcc2.beans.PontoDeOnibus}] contendo os dados da tupla do objeto ResultSet.
     * @throws SQLException
     */
    private PontoDeOnibus getPontoDeOnibus(ResultSet result) throws SQLException{
    	if(result == null) {
    		return null;
    	}
    	
        PontoDeOnibus pontoDeOnibus = new PontoDeOnibus();

        pontoDeOnibus.setLineCode   (result.getString("codigo_linha"));
        pontoDeOnibus.setAddress    (result.getString("endereco"));
        pontoDeOnibus.setNum        (result.getInt   ("numero_ponto"));
        pontoDeOnibus.setLat        (result.getDouble("lat"));
        pontoDeOnibus.setLon        (result.getDouble("lon"));
        pontoDeOnibus.setSeq        (result.getInt   ("seq"));
        pontoDeOnibus.setSgroup		(result.getString("sgroup"));
        pontoDeOnibus.setDirection  (result.getString("direcao"));
        pontoDeOnibus.setType       (result.getString("tipo"));
        pontoDeOnibus.setGid        (result.getInt   ("gid"));
        pontoDeOnibus.setGeom       (result.getString("geom"));

        return pontoDeOnibus;
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
        	 .where("P.codigo_linha = L.codigo_linha ORDER BY P.numero_ponto");
        
        return DAOBaseUTFPR.QueryExecutor(query);
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
    	
    	return DAOBaseUTFPR.QueryExecutor(query);
    }
    
    /*
     * 
     */
    public JSONArray consultarBairros() {
    	QueryMaker query = new QueryMaker();
    	query.select("DISTINCT nome")
    		 .from("divisa_de_bairros")
    		 .orderBy("nome");
    	
    	return DAOBaseUTFPR.QueryExecutor(query);
    }
    
    /*
     * 
     */
    public JSONArray consultarLinhas() {
    	QueryMaker query = new QueryMaker();
    	query.select("DISTINCT codigo_linha", "nome_linha")
    		 .from("linhas_de_onibus ")
    		 .orderBy("codigo_linha");
    	
    	return DAOBaseUTFPR.QueryExecutor(query);
    }
    
    /*
     * 
     */
    public JSONArray consultarCategoriasDeLinhas() {
    	QueryMaker query = new QueryMaker();
    	query.select("DISTINCT categoria")
    		 .from("linhas_de_onibus  ")
    		 .orderBy("categoria");
    	
    	return DAOBaseUTFPR.QueryExecutor(query);
    }
    
    public JSONArray consultarItinerarios() {
    	
    	String clausuraWith = "WITH tabela_aux AS (SELECT codigo_linha, MAX(shape_len) AS tamanho FROM itinerarios_de_onibus GROUP BY codigo_linha)"; 
    	
    	QueryMaker query = new QueryMaker();
    	String consultaPrincipal = 
    			query.select("nome_linha", "codigo_linha", "nome_categoria", "ST_AsGeoJSON(ST_Transform(ST_SetSRID(geom, 29192), 4326), 15, 0) as geojson")
		    		 .from("itinerarios_de_onibus ")
		    		 .where("shape_len", "(SELECT tabela_aux.tamanho FROM tabela_aux WHERE tabela_aux.codigo_linha = itinerarios_de_onibus.codigo_linha)")
		    		 .orderBy("nome_linha;")
		    		 .getQuery();
    	
    	String queryCompleta = clausuraWith + " " + consultaPrincipal;
    	
    	try {
    		PreparedStatement statement = connection.prepareStatement(queryCompleta);
    		ResultSet result = statement.executeQuery();
    		return Parser.toJSON(result);
    	} catch(SQLException sqle) {
    		throw new DatabaseException("Falha ao executar consulta por Itinerários de Ônibus", sqle.getMessage());
    	}
    }
    
    /*
     * 
     */
    public JSONArray consultarHorarios() {
    	QueryMaker query = new QueryMaker();
    	query.select("codigo_linha", "horario_saida", "ponto", "tipo_dia", "numero_ponto", "adaptado")
    		 .from("horarios_de_onibus")
    		 .orderBy("codigo_linha", "ponto", "tipo_dia", "horario_saida");
    	
    	return DAOBaseUTFPR.QueryExecutor(query);
    }
}
