package com.tcc2.geral;

import java.util.logging.Logger;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.tcc2.database.DAOBaseUTFPR;

@WebListener
public class InicializarRecursosContexto implements ServletContextListener{
	
	private static final Logger LOG = Logger.getLogger(InicializarRecursosContexto.class.getName());
	private DAOBaseUTFPR daoUTFPR;
	
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		LOG.info("Inicializando Conexão SSH com base de dados da UTFPR.");
		daoUTFPR = new DAOBaseUTFPR(false);
		daoUTFPR.getConnectionFactory().openConnectionWithSSH();

		LOG.info("Disponibilizando DAOBaseUTFPR no Contexto de Aplicação.");
		ServletContext context = sce.getServletContext();
		context.setAttribute("daoUTFPR", daoUTFPR);
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		LOG.info("Encerrando Conexão SSH com base de dados da UTFPR");
		daoUTFPR.getConnectionFactory().closeConnectionWithSSH();
	}
	
}
