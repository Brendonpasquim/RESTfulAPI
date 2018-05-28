package com.tcc2.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.tcc2.beans.PontoDeOnibus;
import br.com.starmetal.database.ConnectionFactory;
import br.com.starmetal.exceptions.DatabaseException;
import br.com.starmetal.io.IOArquivo;
import br.com.starmetal.io.IOProperties;

public class DAOPontoOnibus {

    private static Connection connection;

    public DAOPontoOnibus(){
        connection = new ConnectionFactory().getDefaultConnectionWithSSH();
    }

    public List<PontoDeOnibus> consultarPontosDeOnibus(String query){

        if(query == null || query.isEmpty()){
            return null;
        }

        List<PontoDeOnibus> listaDePontos = new ArrayList<>();

        try{
            PreparedStatement statement = connection.prepareStatement("select * from myurb_linestop where line_code = '020' limit 5;");
            ResultSet result = statement.executeQuery();
            
            while(result.next()){
                PontoDeOnibus pontoDeOnibus = getPontoDeOnibus(result);
                listaDePontos.add(pontoDeOnibus);
            }

        } catch(SQLException sqle){
            throw new DatabaseException("Falha ao executar consulta por Pontos de Ônibus na base de Dados: " + sqle.getMessage());
        }

        return listaDePontos;
    }

    private PontoDeOnibus getPontoDeOnibus(ResultSet result) throws SQLException{

    	if(result == null) {
    		
    	}
    	
        PontoDeOnibus pontoDeOnibus = new PontoDeOnibus();

        pontoDeOnibus.setLineCode   (result.getString("line_code"));
        pontoDeOnibus.setAddress    (result.getString("address"));
        pontoDeOnibus.setNum        (result.getInt   ("num"));
        pontoDeOnibus.setLat        (result.getDouble("lat"));
        pontoDeOnibus.setLon        (result.getDouble("lon"));
        pontoDeOnibus.setSeq        (result.getInt   ("seq"));
        pontoDeOnibus.setDirection  (result.getString("direction"));
        pontoDeOnibus.setType       (result.getString("type"));
        pontoDeOnibus.setGid        (result.getInt   ("gid"));
        pontoDeOnibus.setGeom       (result.getString("geom"));

        return pontoDeOnibus;
    }
    
    public List<PontoDeOnibus> consultarPontosDeOnibusProximos(double latitude, double longitude){
    	
    	String query =  "SELECT * " +
    					"FROM public.myurb_linestop " +
    					"WHERE ST_Within(geom, ST_buffer(ST_GeomFromText('POINT(? ?)', 4326), 0.01))";
    	
    	List<PontoDeOnibus> listaDePontos = new ArrayList<>();
    	
    	try {
        	PreparedStatement statement = connection.prepareStatement(query);
        	statement.setDouble(1, latitude);
        	statement.setDouble(2, longitude);
        	
        	ResultSet result = statement.executeQuery();
        	while(result.next()) {
        		PontoDeOnibus pontoDeOnibus = getPontoDeOnibus(result);
        		listaDePontos.add(pontoDeOnibus);
        	}
        	
    	} catch(SQLException sqle) {
    		throw new DatabaseException("Falha ao executar consulta por Pontos de Ônibus próximos");
    	}
    	
    	return null;
    }
    
    public static void main(String[] args) {

//        String path = IOProperties.DEFAULT_PROPERTIES_FOLDER_PATH + IOArquivo.SEPARADOR_DE_DIRETORIO + "db.properties";

//        Properties properties = IOProperties.getProperties(path);

        DAOPontoOnibus dao = new DAOPontoOnibus();

        List<PontoDeOnibus> lista = dao.consultarPontosDeOnibus("dummy");

        for(PontoDeOnibus ponto : lista){
            System.out.println(ponto.getLineCode() + ";" +
                                ponto.getLat() + ";" +
                                ponto.getLon() + ";" +
                                ponto.getNum());
        }
        
        new ConnectionFactory().closeDefaultConnectionWithSSH();
    }
}
