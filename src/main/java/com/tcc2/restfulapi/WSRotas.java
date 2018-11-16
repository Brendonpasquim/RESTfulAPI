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
	private DAOManager manager;
	
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getIt() {
		return "RESOURCE ROTAS OK";
	}
	
	@GET
	@Path("rota_simples")
	@Produces(MediaType.APPLICATION_JSON)
	public String consultarRotaSimples(@NotNull @QueryParam("latitude_origem")   double latitudeOrigem,
									   @NotNull @QueryParam("longitude_origem")  double longitudeOrigen,
									   @NotNull @QueryParam("latitude_destino")  double latitudeDestino,
									   @NotNull @QueryParam("longitude_destino") double longitudeDestino) {
		
		JSONArray rotaSimples = manager.getDAORotas().consultarRotaSimplesEntrePontosProximos(latitudeOrigem, longitudeOrigen, latitudeDestino, longitudeDestino);
		return rotaSimples.toString();
	}
	
	@GET
	@Path("rota_simples_dois_pontos")
	@Produces(MediaType.APPLICATION_JSON)
	public String consultarRotaSimplesEntreDoisPontos(@NotNull @QueryParam("numero_ponto_origem")  int numeroPontoOrigem,
													  @NotNull @QueryParam("numero_ponto_destino") int numeroPontoDestino) {
		
		JSONArray rotaSimplesEntreDoisPontos = manager.getDAORotas().consultarRotaSimplesEntreDoisPontos(numeroPontoOrigem, numeroPontoDestino);
		return rotaSimplesEntreDoisPontos.toString();
	}

	@GET
	@Path("rota_conectada")
	@Produces(MediaType.APPLICATION_JSON)
	public String consultarRotaConectada(@NotNull @QueryParam("numero_ponto_origem")  int numeroPontoOrigem,
										 @NotNull @QueryParam("numero_ponto_destino") int numeroPontoDestino) {
		
		JSONArray rotaConectada = manager.getDAORotas().consultarRotaConectada(numeroPontoOrigem, numeroPontoDestino);
		return rotaConectada.toString();
	}
	
	@GET
	@Path("origem")
	@Produces(MediaType.APPLICATION_JSON)
	public String consultarOrigemViagem() {
		
		JSONArray origemViagem = manager.getDAORotas().consultarOrigem();
		return origemViagem.toString();
	}
	
	@GET
	@Path("destino")
	@Produces(MediaType.APPLICATION_JSON)
	public String consultarDestinoViagem() {
		
		JSONArray destinoViagem = manager.getDAORotas().consultarDestino();
		return destinoViagem.toString();
	}
	
}
