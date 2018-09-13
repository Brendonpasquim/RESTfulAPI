package com.tcc2.geral;

import javax.inject.Inject;
import javax.servlet.ServletContext;

import org.glassfish.hk2.api.Factory;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

import com.tcc2.database.DAOManager;

public class DAOManagerFactory implements Factory<DAOManager>{

	private ServletContext context;
	
	@Inject
	public DAOManagerFactory(ServletContext context) {
		this.context = context;
	}
	
	@Override
	public DAOManager provide() {
		return new DAOManager(this.context);
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
