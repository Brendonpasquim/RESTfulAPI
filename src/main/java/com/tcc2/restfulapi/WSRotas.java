package com.tcc2.restfulapi;

import javax.ws.rs.Path;
import javax.ws.rs.core.Context;

import com.tcc2.database.DAOManager;

@Path("rotas")
public class WSRotas {

	@Context
	DAOManager manager;

}
