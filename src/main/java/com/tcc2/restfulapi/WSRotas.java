package com.tcc2.restfulapi;

import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.json.JSONArray;

import com.tcc2.database.DAOManager;

@Path("rotas")
public class WSRotas {

	@Context
	DAOManager manager;
	
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getIt() {
		return "RESOURCE ROTAS OK";
	}
	
	@GET
	@Path("rota_simples")
	@Produces(MediaType.APPLICATION_JSON)
	public String consultarRotaSimples(@NotNull @QueryParam("latitude_origem")   double latitudeOrigem,
									   @NotNull @QueryParam("longitude_origen")  double longitudeOrigen,
									   @NotNull @QueryParam("latitude_destino")  double latitudeDestino,
									   @NotNull @QueryParam("longitude_destino") double longitudeDestino) {
		
		JSONArray rotaSimples = manager.getDAORotas().consultarRotaSimples(latitudeOrigem, longitudeOrigen, latitudeDestino, longitudeDestino);
		return rotaSimples.toString();
	}

}
