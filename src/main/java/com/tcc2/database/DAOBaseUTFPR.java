package com.tcc2.database;

import java.util.logging.Logger;

import com.tcc2.geral.Deploy;

import br.com.starmetal.database.ConnectionFactoryWithSSH;
import br.com.starmetal.database.PooledConnectionFactory;
import br.com.starmetal.io.IOProperties;

public class DAOBaseUTFPR {
	
	/**
	 * Fornece uma Factory de conexões para base de dados via SSH. O SSH e o banco de dados são configurados
	 * considerando os arquivos ssh.properties e db.properties disponíveis no projeto.
	 * <p>As conexões para o servidor da UTFPR devem ser obtidas exclusivamente através do objeto factory.</p>
	 */
	private final ConnectionFactoryWithSSH factory;
	private static final Logger LOG = Logger.getLogger(DAOBaseUTFPR.class.getName());
	
	public DAOBaseUTFPR(final boolean PRODUCTION_MODE) {
		PooledConnectionFactory pool = new PooledConnectionFactory(1, 3, 3, 20, 300, 240);
		
		if(PRODUCTION_MODE) {
			LOG.info("Modo de Operação Atual: [PRODUCTION]");
			factory = new ConnectionFactoryWithSSH(pool,
												   IOProperties.getProperties(System.getenv("HOME") + "/src/main/webapp/WEB-INF/properties/db.properties"), 
					   							   IOProperties.getProperties(System.getenv("HOME") + "/src/main/webapp/WEB-INF/properties/ssh.properties"));
		} else {
			LOG.info("Modo de Operação Atual: [DEVELOPMENT]");
			factory = new ConnectionFactoryWithSSH(pool,
												   IOProperties.getProperties(Deploy.WildFly.DB_CONFIG_FILE_PATH), 
					   							   IOProperties.getProperties(Deploy.WildFly.SSH_CONFIG_FILE_PATH));	
		}
	}
	
	public ConnectionFactoryWithSSH getConnectionFactory() {
		return this.factory;
	}
}
