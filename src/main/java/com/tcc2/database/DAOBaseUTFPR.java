package com.tcc2.database;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.tcc2.geral.Deploy;

import br.com.starmetal.database.ConnectionFactoryWithSSH;
import br.com.starmetal.io.IOProperties;

public class DAOBaseUTFPR {
	

//	public static final ConnectionFactoryWithSSH factory = new ConnectionFactoryWithSSH(IOProperties.getProperties(Constantes.DB_CONFIG_FILE_PATH), 
//																						IOProperties.getProperties(Constantes.SSH_CONFIG_FILE_PATH));

//	public static final ConnectionFactoryWithSSH FACTORY = new ConnectionFactoryWithSSH(IOProperties.getProperties(System.getenv("HOME") + "/src/main/webapp/WEB-INF/properties/db.properties"), 
//	IOProperties.getProperties(System.getenv("HOME") + "/src/main/webapp/WEB-INF/properties/ssh.properties"));

	/**
	 * Fornece uma Factory de conexões para base de dados via SSH. O SSH e o banco de dados são configurados
	 * considerando os arquivos ssh.properties e db.properties disponíveis no projeto.
	 * <p>As conexões para o servidor da UTFPR devem ser obtidas exclusivamente através do objeto factory.</p>
	 */
	private final ConnectionFactoryWithSSH factory;
	
	public DAOBaseUTFPR(final boolean PRODUCTION_MODE) {
		
		if(PRODUCTION_MODE) {
			Logger.getLogger(DAOBaseUTFPR.class.getName()).log(Level.INFO, "Modo de Operação Atual: [PRODUCTION]");
			factory = new ConnectionFactoryWithSSH(IOProperties.getProperties(System.getenv("HOME") + "/src/main/webapp/WEB-INF/properties/db.properties"), 
					   							   IOProperties.getProperties(System.getenv("HOME") + "/src/main/webapp/WEB-INF/properties/ssh.properties"));
		} else {
			Logger.getLogger(DAOBaseUTFPR.class.getName()).log(Level.INFO, "Modo de Operação Atual: [DEVELOPMENT]");
			factory = new ConnectionFactoryWithSSH(IOProperties.getProperties(Deploy.WildFly.DB_CONFIG_FILE_PATH), 
					   							   IOProperties.getProperties(Deploy.WildFly.SSH_CONFIG_FILE_PATH));	
		}
	}
	
	public ConnectionFactoryWithSSH getConnectionFactory() {
		return this.factory;
	}
}
