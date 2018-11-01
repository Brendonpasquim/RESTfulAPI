package com.tcc2.filtros;

import java.io.IOException;
import java.sql.Connection;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

import com.tcc2.database.DAOBaseUTFPR;

@WebFilter("/*")
public class FiltroInjectDBConnection implements Filter{
	
	private static final Logger LOG = Logger.getLogger(FiltroInjectDBConnection.class.getName());
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		ServletContext context = request.getServletContext();
		
		//Recupera o recurso de contexto
		DAOBaseUTFPR daoUTFPR = (DAOBaseUTFPR) context.getAttribute("daoUTFPR");

		LOG.info("Estabelecendo conexão com Base de Dados da UTFPR.");
		try(Connection conexaoBD = daoUTFPR.getConnectionFactory().getConnectionWithSSH()) {			
			//Insere a conexão no contexto
			LOG.info("Disponibilizando conexão com Base de Dados no Contexto de Aplicação.");
			request.setAttribute("connection", conexaoBD);
			
			//Encaminha a execução para o resource de destino.
			chain.doFilter(request, response);
		
			LOG.info("Encerrando conexão com Base de Dados da UTFPR.");
		} catch (Exception e) {			
			LOG.log(Level.SEVERE, "Exceção capturada em Filtro de Conexões de Base de Dados", e);
		}
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {}

	@Override
	public void destroy() {}
}
