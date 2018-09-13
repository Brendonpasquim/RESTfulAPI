package com.tcc2.database;

import java.sql.Connection;

import javax.servlet.ServletContext;

public class DAOManager {
	private Connection connection = null;	
	private DAORotas daoRotas = null;
	private DAOPontos daoPontos = null;
	private QueryExecutor queryExecutor;
	
	public DAOManager(Connection connection) {
		this.connection = connection;
		this.queryExecutor = new QueryExecutor(connection);
	}
	
	public DAOManager(ServletContext context) {
		this.connection = (Connection) context.getAttribute("connection");
		this.queryExecutor = new QueryExecutor(this.connection);
	}
	
	public DAORotas getDAORotas() {
		if(this.daoRotas == null) {
			return this.daoRotas = new DAORotas(this);
		}
		
		return this.daoRotas;
	}
	
	public DAOPontos getDAOPontos() {
		if(this.daoPontos == null) {
			return this.daoPontos = new DAOPontos(this);
		}
		
		return this.daoPontos;
	}
	
	public Connection getConnection() {
		return connection;
	}
	
	public QueryExecutor getQueryExecutor() {
		return queryExecutor;
	}
}
