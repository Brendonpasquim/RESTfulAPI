package com.tcc2.geral;

import java.util.logging.Level;
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
		LOG.log(Level.INFO, "Inicializando CONTEXTO");
		daoUTFPR = new DAOBaseUTFPR(true);
		daoUTFPR.getConnectionFactory().openConnectionWithSSH();
		
		ServletContext context = sce.getServletContext();
		LOG.log(Level.INFO, "Disponibilizando conexão com Base de Dados no Contexto de Aplicação.");
		context.setAttribute("daoUTFPR", daoUTFPR);
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		LOG.log(Level.INFO, "Encerrando CONTEXTO");
		daoUTFPR.getConnectionFactory().closeConnectionWithSSH();
	}
	
}
