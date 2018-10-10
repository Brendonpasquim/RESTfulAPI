package com.tcc2.geral;

import org.glassfish.jersey.server.ResourceConfig;

import parameters.LocalDateConverterProvider;
import parameters.LocalTimeConverterProvider;

public class MyUrbResourceRegister extends ResourceConfig{

	public MyUrbResourceRegister() {
		register(new DAOManagerFactory.Binder());
		packages(true, "com.tcc2.geral");
		
		//Registra a conversão de tipo personalizado.
		register(LocalDateConverterProvider.class);
		register(LocalTimeConverterProvider.class);
		packages(true, "com.tcc2.parameters");
	}
	
}
