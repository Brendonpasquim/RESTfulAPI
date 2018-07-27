package com.tcc2.database;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.tcc2.geral.Constantes;

import br.com.starmetal.database.ConnectionFactoryWithSSH;
import br.com.starmetal.exceptions.BusinessException;
import br.com.starmetal.io.IOProperties;
import br.com.starmetal.validacoes.Validacao;

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
	public static ConnectionFactoryWithSSH factory = isProductionMode() ? getProductionModeConnection() : getDevelopmentModeConnection();
	
	private static boolean isProductionMode() {
		Logger.getLogger(Validacao.class.getName()).log(Level.INFO, "Verificando Modo de Operação...");
		
		String propertiesLocal = IOProperties.getProperties(Constantes.DEFAULT_CONFIG_FILE_PATH).getProperty("config.production_mode");
		String propertiesNuvem = null;
		
		if(!Validacao.ehStringVazia(System.getenv("HOME"))) {
			propertiesNuvem = IOProperties.getProperties(System.getenv("HOME") + "/src/main/webapp/WEB-INF/properties/configs.properties").getProperty("config.production_mode");
		}
		
		if(Validacao.ehStringVazia(propertiesLocal) && Validacao.ehStringVazia(propertiesNuvem)) {
			throw new BusinessException("Não foi possível localicar o arquivo de propriedades 'configs'");
		}
		
		if(!Validacao.ehStringVazia(propertiesNuvem) && propertiesNuvem.equalsIgnoreCase("true")) {
			return true;
		}
		
		return false;
	}
	
	private static ConnectionFactoryWithSSH getProductionModeConnection() {
		Logger.getLogger(Validacao.class.getName()).log(Level.INFO, "Modo de Operação Atual: [PRODUCTION]");
		Logger.getLogger(Validacao.class.getName()).log(Level.INFO, String.format("Variável de Ambiente PROJECT_PATH '%s'", System.getenv("HOME")));
		return new ConnectionFactoryWithSSH(IOProperties.getProperties(System.getenv("HOME") + "/src/main/webapp/WEB-INF/properties/db.properties"), 
				   							IOProperties.getProperties(System.getenv("HOME") + "/src/main/webapp/WEB-INF/properties/ssh.properties"));
	}
	
	private static ConnectionFactoryWithSSH getDevelopmentModeConnection() {
		Logger.getLogger(Validacao.class.getName()).log(Level.INFO, "Modo de Operação Atual: [DEVELOPMENT]");
		return new ConnectionFactoryWithSSH(IOProperties.getProperties(Constantes.DB_CONFIG_FILE_PATH), 
				   							IOProperties.getProperties(Constantes.SSH_CONFIG_FILE_PATH));
	}

	

}
