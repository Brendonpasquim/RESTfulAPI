package testes;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import com.jcraft.jsch.JSchException;

import br.com.starmetal.database.ConnectionFactory;
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
        ssh.setLocalHostPort(5432);
        ssh.setRemoteHostPort(5434);
        
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
	
	public static void main(String[] args) {
		TestesUnitarios.TesteIO();
//		TestesUnitarios.TesteSSH();
	}

}
