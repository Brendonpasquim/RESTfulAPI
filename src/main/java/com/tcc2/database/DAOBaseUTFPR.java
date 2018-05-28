package com.tcc2.database;

import java.sql.Connection;

import com.tcc2.geral.Constantes;

import br.com.starmetal.database.ConnectionFactory;

public class DAOBaseUTFPR {
	
	private static Connection connection;
	
	public DAOBaseUTFPR() {
		connection = new ConnectionFactory().getDefaultConnectionWithSSH();
	}

}
