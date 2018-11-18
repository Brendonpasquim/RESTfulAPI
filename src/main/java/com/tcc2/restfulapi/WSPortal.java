package com.tcc2.restfulapi;

import static com.tcc2.restfulapi.Responses.getStatusGET;

import java.time.LocalDate;
import java.time.LocalTime;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONArray;

import com.tcc2.database.DAOManager;

@Path("portal")
public class WSPortal {

	@Context
	private DAOManager manager;
	
	@GET
	@Path("mapa_origem")
	@Produces(MediaType.APPLICATION_JSON)
	public Response consultarMapaOrigem(@QueryParam("dia_inicio")  LocalDate diaInicio,
									    @QueryParam("dia_fim") 	   LocalDate diaFim,
									    @QueryParam("hora_inicio") LocalTime horaInicio,
									    @QueryParam("hora_fim")    LocalTime horaFim,
									    @QueryParam("bairro")	   String bairro) {
		
		JSONArray mapaOrigem = manager.getDAOPortal().consultarMapaOrigem(diaInicio, diaFim, horaInicio, horaFim, bairro);
		return getStatusGET(mapaOrigem);
	}
	
	@GET
	@Path("mapa_destino")
	@Produces(MediaType.APPLICATION_JSON)
	public Response consultarMapaDestino(@QueryParam("dia_inicio")  LocalDate diaInicio,
									     @QueryParam("dia_fim") 	LocalDate diaFim,
									     @QueryParam("hora_inicio") LocalTime horaInicio,
									     @QueryParam("hora_fim")    LocalTime horaFim,
									     @QueryParam("bairro")	    String bairro) {
		
		JSONArray mapaOrigem = manager.getDAOPortal().consultarMapaDestino(diaInicio, diaFim, horaInicio, horaFim, bairro);
		return getStatusGET(mapaOrigem);
	}
	
	@GET
	@Path("relatorio_linhas")
	@Produces(MediaType.APPLICATION_JSON)
	public Response consultarRelatorioLinhas() {
		
		JSONArray relatorioLinhas = manager.getDAOPortal().consultarRelatorioLinhas();
		return getStatusGET(relatorioLinhas);
	}
	
	@GET
	@Path("relatorio_pontos_origem")
	@Produces(MediaType.APPLICATION_JSON)
	public Response consultarRelatorioPontosOrigem() {
		
		JSONArray relatorioPontosOrigem = manager.getDAOPortal().consultarRelatorioPontosOrigem();
		return getStatusGET(relatorioPontosOrigem);
	}
	
	@GET
	@Path("relatorio_pontos_destino")
	@Produces(MediaType.APPLICATION_JSON)
	public Response consultarRelatorioPontosDestino() {
		
		JSONArray relatorioPontosDestino = manager.getDAOPortal().consultarRelatorioPontosDestino();
		return getStatusGET(relatorioPontosDestino);
	}
	
	@GET
	@Path("crowdsourcing_pontos_mapa")
	@Produces(MediaType.APPLICATION_JSON)
	public Response consultarCrowdsourcingPontosMapa(@QueryParam("dia_inicio") 	LocalDate diaInicio,
									   			     @QueryParam("dia_fim") 	LocalDate diaFim,
									   			     @QueryParam("hora_inicio") LocalTime horaInicio,
									   			     @QueryParam("hora_fim")    LocalTime horaFim,
									   			     @QueryParam("bairro")	   	String bairro) {
		
		JSONArray mapaOrigem = manager.getDAOPortal().consultarCrowdsourcingPontosMapa(diaInicio, diaFim, horaInicio, horaFim, bairro);
		return getStatusGET(mapaOrigem);
	}
	
	@GET
	@Path("crowdsourcing_regras_relatorio")
	@Produces(MediaType.APPLICATION_JSON)
	public Response consultarCrowdsourcingRegrasRelatorio() {
		
		JSONArray crowdsourcingRegrasRelatorio = manager.getDAOPortal().consultarCrowdsourcingRegrasRelatorio();
		return getStatusGET(crowdsourcingRegrasRelatorio);
	}
	
	@GET
	@Path("crowdsourcing_linhas_mapas")
	@Produces(MediaType.APPLICATION_JSON)
	public Response consultarCrowdsourcingLinhasMapa() {
		
		JSONArray crowdsourcingLinhasMapas = manager.getDAOPortal().consultarCrowdsourcingLinhasMapa();
		return getStatusGET(crowdsourcingLinhasMapas);
	}
}
