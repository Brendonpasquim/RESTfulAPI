package com.tcc2.geral;

import static br.com.starmetal.io.IOArquivo.SEPARADOR_DE_DIRETORIO;

/**
 * Classe que contém os PATHs para os arquivos de configuração do projeto, considerado
 * o container de aplicação utilizado (Tomcat, WildFly, etc).
 * 
 * @author Brendon
 *
 */
public class Deploy {
	
	public static class Tomcat{
		/**
		 * Pasta em que ocorre a implantação (deploy) do projeto, ao utilizar-se Tomcat.
		 */
		public static final String DEPLOYMENT_FOLDER_PATH 	= System.getProperty("catalina.base");
		
		/**
		 * Caminho para o arquivo de configuração padrão do projeto, quando implantado com Tomcat.
		 */
		public static final String DEFAULT_CONFIG_FILE_PATH = DEPLOYMENT_FOLDER_PATH + SEPARADOR_DE_DIRETORIO + "wtpwebapps\\restfulapi\\WEB-INF\\properties\\configs.properties";
		
		/**
		 * Caminho para o arquivo de configuração de SSH do projeto, quando implantado com Tomcat.
		 */
		public static final String SSH_CONFIG_FILE_PATH 	= DEPLOYMENT_FOLDER_PATH + SEPARADOR_DE_DIRETORIO + "wtpwebapps\\restfulapi\\WEB-INF\\properties\\ssh.properties";
		
		/**
		 * Caminho para o arquivo de configuração de Database do projeto, quando implantado com Tomcat.
		 */
		public static final String DB_CONFIG_FILE_PATH 		= DEPLOYMENT_FOLDER_PATH + SEPARADOR_DE_DIRETORIO + "wtpwebapps\\restfulapi\\WEB-INF\\properties\\db.properties";
	}
	
	public static class WildFly{
		/**
		 * Pasta em que ocorre a implantação (deploy) do projeto, ao utilizar-se WildFly
		 */
		public static final String DEPLOYMENT_FOLDER_PATH 	= System.getProperty("jboss.server.base.dir");
		
		/**
		 * Caminho para o arquivo de configuração padrão do projeto, quando implantado com WildFly.
		 */
		public static final String DEFAULT_CONFIG_FILE_PATH = DEPLOYMENT_FOLDER_PATH + SEPARADOR_DE_DIRETORIO + "deployments\\restfulapi.war\\WEB-INF\\properties\\configs.properties";
		
		/**
		 * Caminho para o arquivo de configuração de SSH do projeto, quando implantado com WildFly.
		 */
		public static final String SSH_CONFIG_FILE_PATH 	= DEPLOYMENT_FOLDER_PATH + SEPARADOR_DE_DIRETORIO + "deployments\\restfulapi.war\\WEB-INF\\properties\\ssh.properties";
		
		/**
		 * Caminho para o arquivo de configuração de Database do projeto, quando implantado com WildFly.
		 */
		public static final String DB_CONFIG_FILE_PATH 		= DEPLOYMENT_FOLDER_PATH + SEPARADOR_DE_DIRETORIO + "deployments\\restfulapi.war\\WEB-INF\\properties\\db.properties";
	}	
}
