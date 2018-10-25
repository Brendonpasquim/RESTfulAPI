package com.tcc2.filtros;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
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

import br.com.starmetal.exceptions.DatabaseException;

@WebFilter("/*")
public class FiltroInjectDBConnection implements Filter{
	
	private static final Logger LOG = Logger.getLogger(FiltroInjectDBConnection.class.getName());
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		ServletContext context = request.getServletContext();
		
		//Recupera o recurso de contexto
		LOG.info("Estabelecendo conexão com Base de Dados da UTFPR.");
		DAOBaseUTFPR daoUTFPR = (DAOBaseUTFPR) context.getAttribute("daoUTFPR");
		Connection conexaoBD = daoUTFPR.getConnectionFactory().getConnectionWithSSH();
		
		//Insere a conexão no contexto
		LOG.info("Disponibilizando conexão com Base de Dados no Contexto de Aplicação.");
		context.setAttribute("connection", conexaoBD);
		
		//Encaminha a execução para o resource de destino.
		chain.doFilter(request, response);
		
		try {
			LOG.info("Encerrando conexão com Base de Dados da UTFPR.");
			conexaoBD.close();
		} catch (SQLException sqle) {
			throw new DatabaseException("Falha ao encerrar conexão de banco de dados de contexto", sqle.getMessage());
		}
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {}

	@Override
	public void destroy() {}
}
