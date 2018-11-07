package com.tcc2.restfulapi;

import java.time.LocalDate;
import java.time.LocalTime;

import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.tcc2.database.DAOManager;

import br.com.starmetal.results.ResultType;

@Path("crowdsourcing")
public class WSCrowdsourcing {

	@Context
	private DAOManager manager;
	
	@POST
	@Path("cadastrar_relatorio_viagem")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public Response cadastrarRelatorioViagem(@NotNull @FormParam("data_viagem") 	LocalDate dataViagem,
											 @NotNull @FormParam("horario_chegada") LocalTime horarioChegada,
											 @NotNull @FormParam("horario_saida") 	LocalTime horarioSaida,
											 @NotNull @FormParam("ponto_chegada") 	int pontoChegada,
											 @NotNull @FormParam("ponto_saida") 	int pontoSaida,
											 @NotNull @FormParam("codigo_linha") 	String codigoLinha) {
		
		ResultType response = manager.getDAOCrowdsourcing().cadastrarRelatorioViagem(dataViagem, horarioSaida, pontoSaida, horarioChegada, pontoChegada, codigoLinha);
		return response.equals(ResultType.SUCESS) ? Response.status(Status.CREATED).build() : Response.status(Status.BAD_REQUEST).build();
	}

	@POST
	@Path("cadastrar_crowdsourcing_pontos")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public Response cadastrarCrowdsourcingPontos(@NotNull @FormParam("tipo") 		 int tipo,
												 @NotNull @FormParam("numero_ponto") int numeroPonto,
												 @NotNull @FormParam("dia") 		 LocalDate dia,
												 @NotNull @FormParam("horario") 	 LocalTime horario) {
		
		ResultType response = manager.getDAOCrowdsourcing().cadastrarCrowdsourcingPontos(numeroPonto, dia, horario, tipo);
		return response.equals(ResultType.SUCESS) ? Response.status(Status.CREATED).build() : Response.status(Status.BAD_REQUEST).build();  
	}
	
	@POST
	@Path("cadastrar_crowdsourcing_linhas")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public Response cadastrarCrowdsourcingLinhas(@NotNull @FormParam("tipo") 		 int tipo,
												 @NotNull @FormParam("codigo_linha") String codigoLinha,
												 @NotNull @FormParam("dia") 		 LocalDate dia,
												 @NotNull @FormParam("horario") 	 LocalTime horario,
												 @NotNull @FormParam("lat") 		 double latitude,
												 @NotNull @FormParam("lon") 		 double longitude) {
		
		ResultType response = manager.getDAOCrowdsourcing().cadastrarCrowdsourcingLinhas(codigoLinha, dia, horario, latitude, longitude, tipo);
		return response.equals(ResultType.SUCESS) ? Response.status(Status.CREATED).build() : Response.status(Status.BAD_REQUEST).build();
	}
	
}
