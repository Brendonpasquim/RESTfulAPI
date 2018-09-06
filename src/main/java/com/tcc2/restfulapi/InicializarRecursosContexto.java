package com.tcc2.restfulapi;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.tcc2.database.DAOBaseUTFPR;

@WebListener
public class InicializarRecursosContexto implements ServletContextListener{
	
	private DAOBaseUTFPR daoUTFPR;
	
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		Logger.getLogger(InicializarRecursosContexto.class.getName()).log(Level.INFO, "Inicializando CONTEXTO");
		daoUTFPR = new DAOBaseUTFPR(false);
		daoUTFPR.getConnectionFactory().openConnectionWithSSH();
		
		ServletContext context = sce.getServletContext();
		Logger.getLogger(InicializarRecursosContexto.class.getName()).log(Level.INFO, "Disponibilizando conexão com Base de Dados no Contexto de Aplicação.");
		context.setAttribute("daoUTFPR", daoUTFPR);
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		Logger.getLogger(InicializarRecursosContexto.class.getName()).log(Level.INFO, "Encerrando CONTEXTO");
		daoUTFPR.getConnectionFactory().closeConnectionWithSSH();
	}
	
}
