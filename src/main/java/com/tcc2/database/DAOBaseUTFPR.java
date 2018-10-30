package com.tcc2.database;

import java.util.Properties;
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

		Properties dbPropertie;
		Properties sshPropertie;
				
		if(PRODUCTION_MODE) {
			LOG.info("Modo de Operação Atual: [PRODUCTION]");
			dbPropertie  = IOProperties.getProperties(System.getenv("HOME") + "/src/main/webapp/WEB-INF/properties/db.properties");
			sshPropertie = IOProperties.getProperties(System.getenv("HOME") + "/src/main/webapp/WEB-INF/properties/ssh.properties");
		} else {
			LOG.info("Modo de Operação Atual: [DEVELOPMENT]");
			dbPropertie  = IOProperties.getProperties(Deploy.WildFly.DB_CONFIG_FILE_PATH);
			sshPropertie = IOProperties.getProperties(Deploy.WildFly.SSH_CONFIG_FILE_PATH);
		}
		
		factory = new ConnectionFactoryWithSSH(new PooledConnectionFactory(dbPropertie), dbPropertie, sshPropertie);
	}
	
	public ConnectionFactoryWithSSH getConnectionFactory() {
		return this.factory;
	}
}
