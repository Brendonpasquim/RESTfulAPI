package com.tcc2.geral;

import org.glassfish.jersey.server.ResourceConfig;

public class MyUrbResourceRegister extends ResourceConfig{

	public MyUrbResourceRegister() {
		register(new DAOManagerFactory.Binder());
		packages(true, "com.tcc2.geral");
	}
	
}
