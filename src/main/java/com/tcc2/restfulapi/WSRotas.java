package com.tcc2.restfulapi;

import static com.tcc2.restfulapi.Responses.getStatusGET;

import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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
	public Response consultarRotaSimples(@NotNull @QueryParam("latitude_origem")   double latitudeOrigem,
									     @NotNull @QueryParam("longitude_origem")  double longitudeOrigen,
									     @NotNull @QueryParam("latitude_destino")  double latitudeDestino,
									     @NotNull @QueryParam("longitude_destino") double longitudeDestino) {
		
		JSONArray rotaSimples = manager.getDAORotas().consultarRotaSimplesEntrePontosProximos(latitudeOrigem, longitudeOrigen, latitudeDestino, longitudeDestino);
		return getStatusGET(rotaSimples);
	}
	
	@GET
	@Path("rota_simples_dois_pontos")
	@Produces(MediaType.APPLICATION_JSON)
	public Response consultarRotaSimplesEntreDoisPontos(@NotNull @QueryParam("numero_ponto_origem")  int numeroPontoOrigem,
													    @NotNull @QueryParam("numero_ponto_destino") int numeroPontoDestino) {
		
		JSONArray rotaSimplesEntreDoisPontos = manager.getDAORotas().consultarRotaSimplesEntreDoisPontos(numeroPontoOrigem, numeroPontoDestino);
		return getStatusGET(rotaSimplesEntreDoisPontos);
	}

	@GET
	@Path("rota_conectada")
	@Produces(MediaType.APPLICATION_JSON)
	public Response consultarRotaConectada(@NotNull @QueryParam("numero_ponto_origem")  int numeroPontoOrigem,
										   @NotNull @QueryParam("numero_ponto_destino") int numeroPontoDestino) {
		
		JSONArray rotaConectada = manager.getDAORotas().consultarRotaConectada(numeroPontoOrigem, numeroPontoDestino);
		return getStatusGET(rotaConectada);
	}
	
	@GET
	@Path("origem")
	@Produces(MediaType.APPLICATION_JSON)
	public Response consultarOrigemViagem() {
		
		JSONArray origemViagem = manager.getDAORotas().consultarOrigem();
		return getStatusGET(origemViagem);
	}
	
	@GET
	@Path("destino")
	@Produces(MediaType.APPLICATION_JSON)
	public Response consultarDestinoViagem() {
		
		JSONArray destinoViagem = manager.getDAORotas().consultarDestino();
		return getStatusGET(destinoViagem);
	}
	
}
