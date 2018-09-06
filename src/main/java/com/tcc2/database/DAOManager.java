package com.tcc2.database;

import java.sql.Connection;

import javax.servlet.ServletContext;

public class DAOManager {
	private Connection connection = null;	
	private DAORotas daoRotas = null;
	private DAOPontos daoPontos = null;
	
	public DAOManager(Connection connection) {
		this.connection = connection;
	}
	
	public DAOManager(ServletContext context) {
		this.connection = (Connection) context.getAttribute("connection");
	}
	
	public DAORotas getDAORotas() {
		if(this.daoRotas == null) {
			return this.daoRotas = new DAORotas(this.connection);
		}
		
		return this.daoRotas;
	}
	
	public DAOPontos getDAOPontos() {
		if(this.daoPontos == null) {
			return this.daoPontos = new DAOPontos(this.connection);
		}
		
		return this.daoPontos;
	}
}
