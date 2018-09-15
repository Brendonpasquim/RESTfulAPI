package com.tcc2.testes;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import br.com.starmetal.database.ConnectionFactory;
import br.com.starmetal.database.postgresql.QueryMaker;
import br.com.starmetal.io.IOArquivo;
import br.com.starmetal.io.IOProperties;
import br.com.starmetal.network.SSHConnector;

public class TestesUnitarios {
	
	private static void TesteIO() {
		
		System.out.println(IOArquivo.CAMINHO_PROJETO_ATUAL);
		System.out.println(IOProperties.DEFAULT_PROPERTIES_FILE_PATH);
		System.out.println(IOProperties.DEFAULT_PROPERTIES_FOLDER_PATH);
		
		String sshPath = IOProperties.DEFAULT_PROPERTIES_FOLDER_PATH + IOArquivo.SEPARADOR_DE_DIRETORIO + "ssh.properties";
		System.out.println(sshPath);
		Properties pro = IOProperties.getProperties(sshPath);
		
		System.out.println(pro.getProperty("ssh.username"));
	}
	
	private static void TesteSSH() {
		SSHConnector ssh = new SSHConnector();
        ssh.setUsername("posttunnel1");
        ssh.setPassword("Gr@ngotts");
        ssh.setRemoteHost("200.134.10.21");
        ssh.setLocalHost("localhost");
        ssh.setSSHPort(22);
        ssh.setLocalHostPort(5434);
        ssh.setRemoteHostPort(5432);
        
        try {
        	ssh.connect();
        	
        	ConnectionFactory factory = new ConnectionFactory();
        	
        	String dbName = "BIGSEA";
            String dbUser = "postread";
            String dbPassword = "PostRead";
            String url = "jdbc:postgresql://" + ssh.getLocalHost() + ":" + ssh.getRemoteHostPort() + "/" + dbName;
            
            Connection connection = factory.getConnection(url, dbUser, dbPassword);
            
            System.out.println("Criando query...");
            Statement st = connection.createStatement();
            String query = "SELECT refname FROM transporte.ponto_de_onibus limit 5;";
            
            ResultSet select = st.executeQuery(query);
            while(select.next()){
                String cod = select.getString("refname");
                System.out.println("COD: " + cod);
            }
            System.out.println("Conexão BD encerrada...");
            ssh.disconnect();
            System.out.println("Conexão SSH encerrada...");
        } catch(SQLException sql ) {
        	sql.printStackTrace();
        }
	}
	
//	private static void TesteSSH2() {
//		Connection connection = DAOBaseUTFPR.factory.getConnectionWithSSH();
//		System.out.println(Constantes.SSH_CONFIG_FILE_PATH);
//        try {
//            System.out.println("Criando query...");
//            Statement st = connection.createStatement();
//            String query = "select * from public.pontos_de_onibus limit 5;";
//            
//            ResultSet select = st.executeQuery(query);
//            while(select.next()){
//                String cod = select.getString("gid");
//                System.out.println("COD: " + cod);
//            }
//            System.out.println("Conexão BD encerrada...");
//        } catch(SQLException sql ) {
//        	sql.printStackTrace();
//        }
//		
//	}
	
	private static void TesteQueryMaker() {
        QueryMaker query = new QueryMaker();
        query.select("P.numero_ponto, P.endereco, P.tipo, P.codigo_linha, L.nome_linha, P.seq, L.apenas_cartao, ST_AsGeoJSON(P.geom, 15, 0) as geojson")
        	 .from("pontos_de_onibus P, linhas_de_onibus L")
        	 .where("P.codigo_linha = L.codigo_linha ORDER BY P.numero_ponto");
        
        query.printQuery();
        
        System.out.println();
        
		QueryMaker query2 = new QueryMaker();
		query2.select("A.seq", "A.numero_ponto", "A.codigo_linha", "ST_AsGeoJSON(geom, 15, 0) geojson")
			 .from("pontos_de_onibus A")
			 .where("(A.seq >= (SELECT MIN(seq) FROM pontos_de_onibus WHERE codigo_linha = A.codigo_linha AND numero_ponto = ':X' AND direcao = A.direcao) " + 
			 		"AND A.seq <= (SELECT MAX(seq) FROM pontos_de_onibus WHERE codigo_linha = A.codigo_linha AND numero_ponto = ':Y' AND direcao = A.direcao))")
			 .setParameter("X", 1)
			 .setParameter("Y", 2)
			 .where("A.codigo_linha", String.valueOf(147))
			 .orderBy("codigo_linha, seq");
		
		query2.printQuery();
	}
	
	public static void main(String[] args) {
//		TestesUnitarios.TesteIO();
//		TestesUnitarios.TesteSSH();
//		TestesUnitarios.TesteSSH2();
		TestesUnitarios.TesteQueryMaker();
	}

}
