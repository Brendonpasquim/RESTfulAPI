package com.tcc2.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.tcc2.beans.PontoDeOnibus;

import br.com.starmetal.database.QueryMaker;
import br.com.starmetal.exceptions.DatabaseException;

public class DAOPontoOnibus {

    private static Connection connection;

    public DAOPontoOnibus(){
        connection = DAOBaseUTFPR.factory.getConnectionWithSSH();
    }

    public List<PontoDeOnibus> consultarPontosDeOnibus(){
        List<PontoDeOnibus> listaDePontos = new ArrayList<>();

        try{
            PreparedStatement statement = connection.prepareStatement("select * from public.pontos_de_onibus limit 5;");
            ResultSet result = statement.executeQuery();
            
            while(result.next()){
                PontoDeOnibus pontoDeOnibus = getPontoDeOnibus(result);
                listaDePontos.add(pontoDeOnibus);
            }

        } catch(SQLException sqle){
            throw new DatabaseException("Falha ao executar consulta por Pontos de Ônibus na base de Dados.", sqle.getMessage());
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
     * A partir de uma coordenada latitude e longitude determina Pontos de ônibus próximos,
     * considerando um raio de abrangência arbitrário.
     * 
     * @param latitude [Obrigatório] - Uma coordenada latitude.
     * @param longitude [Obrigatório] - Uma coordenada longitude.
     * @return [java.util.List] contendo uma lista de pontos de ônibus próximos.
     */
    public List<PontoDeOnibus> consultarPontosDeOnibusProximos(double latitude, double longitude){
    	
    	List<PontoDeOnibus> listaDePontos = new ArrayList<>();
    	
//    	String query =  "SELECT * " +
//    					"FROM public.pontos_de_onibus " +
//    					"WHERE ST_Within(geom, ST_buffer(ST_GeomFromText('POINT( #1 #2 )', 4326), 0.01))";
    	
    	QueryMaker query = new QueryMaker();
    	query.select("*")
    		.from("public.pontos_de_onibus")
    		.where("ST_Within(geom, ST_buffer(ST_GeomFromText('POINT( :longitude :latitude )', 4326), 0.01))")
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
    
    public static void main(String[] args) {
    	DAOPontoOnibus.testePontosProximos();
    }
    
    private static void testePontosProximos() {
    	double latitude = -25.43941;
    	double longitude = -49.26862;
    	List<PontoDeOnibus> lista = new DAOPontoOnibus().consultarPontosDeOnibusProximos(latitude, longitude);
    	
        for(PontoDeOnibus ponto : lista){
            System.out.println(ponto.getLineCode() + ";" +
                                ponto.getLat() + ";" +
                                ponto.getLon() + ";" +
                                ponto.getNum());
        }
        
        DAOBaseUTFPR.factory.closeConnectionWithSSH();    	
    }
    
    private static void testeConsultarPontos() {
        List<PontoDeOnibus> lista = new DAOPontoOnibus().consultarPontosDeOnibus();

        for(PontoDeOnibus ponto : lista){
            System.out.println(ponto.getLineCode() + ";" +
                                ponto.getLat() + ";" +
                                ponto.getLon() + ";" +
                                ponto.getNum());
        }
        
        DAOBaseUTFPR.factory.closeConnectionWithSSH();
    }
}
