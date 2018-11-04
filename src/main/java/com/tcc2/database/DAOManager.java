package com.tcc2.database;

import java.sql.Connection;

public class DAOManager {
	private Connection connection = null;
	private DAORotas daoRotas = null;
	private DAOPontos daoPontos = null;
	private DAOCrowdsourcing daoCrowdsourcing = null;
	private DAOPortal daoPortal = null;
	private Executor executor;
	
	public DAOManager(Connection connection) {
		this.connection = connection;
		this.executor = new Executor(connection);
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
	
	public DAOCrowdsourcing getDAOCrowdsourcing() {
		if(this.daoCrowdsourcing == null) {
			return this.daoCrowdsourcing = new DAOCrowdsourcing(this);
		}
		
		return this.daoCrowdsourcing;
	}
	
	public DAOPortal getDAOPortal() {
		if(this.daoPortal == null) {
			return this.daoPortal = new DAOPortal(this);
		}
		
		return this.daoPortal;
	}
	
	public Connection getConnection() {
		return connection;
	}
	
	public Executor getExecutor() {
		return executor;
	}
}
