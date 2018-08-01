package testes.daopontos;

import java.sql.Connection;
import java.util.List;

import com.tcc2.beans.PontoDeOnibus;
import com.tcc2.database.DAOBaseUTFPR;
import com.tcc2.database.DAOPontos;

public class TestesDAOPontoOnibus {
	
	private static Connection connection;
	
	public TestesDAOPontoOnibus() {
		connection = DAOBaseUTFPR.factory.getConnectionWithSSH();
	}
	
	public static void main(String[] args) {
    	TestesDAOPontoOnibus.testePontosProximos();
    }
    
    private static void testePontosProximos() {
    	double latitude = -25.43941;
    	double longitude = -49.26862;
    	List<PontoDeOnibus> lista = new DAOPontos().consultarPontosDeOnibusProximos(latitude, longitude);
    	
        for(PontoDeOnibus ponto : lista){
            System.out.println(ponto.getLineCode() + ";" +
                                ponto.getLat() + ";" +
                                ponto.getLon() + ";" +
                                ponto.getNum());
        }
        
        DAOBaseUTFPR.factory.closeConnectionWithSSH();    	
    }
    
}
