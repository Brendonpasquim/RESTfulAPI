package com.tcc2.geral;

import br.com.starmetal.io.IOArquivo;

public class Constantes {
	
	/**
	 * Pasta em que ocorre a implantação (deploy) do projeto, ao utilizar-se Tomcat.
	 */
	public static final String DEPLOYMENT_FOLDER_PATH = System.getProperty("catalina.base");
	
	/**
	 * Caminho para o arquivo de configuração padrão do projeto, quando implantado com Tomcat.
	 */
	public static final String DEFAULT_CONFIG_FILE_PATH = DEPLOYMENT_FOLDER_PATH + IOArquivo.SEPARADOR_DE_DIRETORIO + "wtpwebapps\\restfulapi\\WEB-INF\\properties\\configs.properties";
	
	/**
	 * Caminho para o arquivo de configuração de SSH do projeto, quando implantado com Tomcat.
	 */
	public static final String SSH_CONFIG_FILE_PATH = DEPLOYMENT_FOLDER_PATH + IOArquivo.SEPARADOR_DE_DIRETORIO + "wtpwebapps\\restfulapi\\WEB-INF\\properties\\ssh.properties";
	
	/**
	 * Caminho para o arquivo de configuração de Database do projeto, quando implantado com Tomcat.
	 */
	public static final String DB_CONFIG_FILE_PATH = DEPLOYMENT_FOLDER_PATH + IOArquivo.SEPARADOR_DE_DIRETORIO + "wtpwebapps\\restfulapi\\WEB-INF\\properties\\db.properties";
}
