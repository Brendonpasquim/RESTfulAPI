package com.tcc2.database;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.tcc2.geral.Constantes;

import br.com.starmetal.database.ConnectionFactoryWithSSH;
import br.com.starmetal.io.IOProperties;

public class DAOBaseUTFPR {
	

//	public static final ConnectionFactoryWithSSH factory = new ConnectionFactoryWithSSH(IOProperties.getProperties(Constantes.DB_CONFIG_FILE_PATH), 
//																						IOProperties.getProperties(Constantes.SSH_CONFIG_FILE_PATH));

//	public static final ConnectionFactoryWithSSH FACTORY = new ConnectionFactoryWithSSH(IOProperties.getProperties(System.getenv("HOME") + "/src/main/webapp/WEB-INF/properties/db.properties"), 
//	IOProperties.getProperties(System.getenv("HOME") + "/src/main/webapp/WEB-INF/properties/ssh.properties"));

	private static final boolean PRODUCTION_MODE = false;
	
	/**
	 * Fornece uma Factory de conexões para base de dados via SSH. O SSH e o banco de dados são configurados
	 * considerando os arquivos ssh.properties e db.properties disponíveis no projeto.
	 * <p>As conexões para o servidor da UTFPR devem ser obtidas exclusivamente através do objeto factory.</p>
	 */
	public static ConnectionFactoryWithSSH factory = getConnectionMode();
	
	private static ConnectionFactoryWithSSH getConnectionMode() {
		
		if(PRODUCTION_MODE) {
			Logger.getLogger(DAOBaseUTFPR.class.getName()).log(Level.INFO, "Modo de Operação Atual: [PRODUCTION]");
			return new ConnectionFactoryWithSSH(IOProperties.getProperties(System.getenv("HOME") + "/src/main/webapp/WEB-INF/properties/db.properties"), 
					   							IOProperties.getProperties(System.getenv("HOME") + "/src/main/webapp/WEB-INF/properties/ssh.properties"));
		} else {
			Logger.getLogger(DAOBaseUTFPR.class.getName()).log(Level.INFO, "Modo de Operação Atual: [DEVELOPMENT]");
			return new ConnectionFactoryWithSSH(IOProperties.getProperties(Constantes.DB_CONFIG_FILE_PATH), 
					   							IOProperties.getProperties(Constantes.SSH_CONFIG_FILE_PATH));	
		}
	}
}
