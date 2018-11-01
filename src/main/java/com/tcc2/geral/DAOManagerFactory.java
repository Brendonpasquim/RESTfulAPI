package com.tcc2.geral;

import java.sql.Connection;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.glassfish.hk2.api.Factory;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

import com.tcc2.database.DAOManager;

public class DAOManagerFactory implements Factory<DAOManager>{

	private HttpServletRequest request;
	
	@Inject
	public DAOManagerFactory(HttpServletRequest request) {
		this.request = request;
	}
	
	@Override
	public DAOManager provide() {
		Connection connection = (Connection) request.getAttribute("connection");
		return new DAOManager(connection);
	}

	@Override
	public void dispose(DAOManager instance) {}

	public static class Binder extends AbstractBinder{

		@Override
		protected void configure() {
			bindFactory(DAOManagerFactory.class).to(DAOManager.class);
		}
		
	}

}
