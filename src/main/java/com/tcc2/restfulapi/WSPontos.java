package com.tcc2.restfulapi;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONArray;

import com.tcc2.beans.PontosProximos;
import com.tcc2.database.DAOManager;

@Path("pontos")
public class WSPontos {
	
	@Context
	DAOManager manager;
	
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getIt() {
        return "Got it 222!";
    }

	@GET
	@Path("pontos_proximos")
	@Produces(MediaType.APPLICATION_JSON)	
	public Response consultarPontosProximos(@NotNull @QueryParam("latitude") double latitude, 
											@NotNull @QueryParam("longitude") double longitude) {
		
        List<PontosProximos> lista = manager.getDAOPontos().consultarPontosDeOnibusProximos(latitude, longitude);
		return Response.ok(lista).build();
	}
	
	@GET
	@Path("pontos")
	@Produces(MediaType.APPLICATION_JSON)
	public String consultarPontos() {
		
		JSONArray pontos = manager.getDAOPontos().consultarPontosDeOnibus();
		return pontos.toString();
	}
	
	@GET
	@Path("tipos_pontos")
	@Produces(MediaType.APPLICATION_JSON)
	public String consultarTiposDePontos() {
		
		JSONArray tipos = manager.getDAOPontos().consultarTiposDePonto();
		return tipos.toString();
	}
	
	@GET
	@Path("bairros")
	@Produces(MediaType.APPLICATION_JSON)
	public String consultarBairros() {
		
		JSONArray bairros = manager.getDAOPontos().consultarBairros();
		return bairros.toString();
	}
	
	@GET
	@Path("linhas")
	@Produces(MediaType.APPLICATION_JSON)
	public String consultarLinhas() {
		
		JSONArray linhas = manager.getDAOPontos().consultarLinhas();
		return linhas.toString();
	}
	
	
	@GET
	@Path("categorias")
	@Produces(MediaType.APPLICATION_JSON)
	public String consultarCategoriasDeLinhas() {
		
		JSONArray categorias = manager.getDAOPontos().consultarCategoriasDeLinhas();
		return categorias.toString();
	}
	

	@GET
	@Path("itinerarios")
	@Produces(MediaType.APPLICATION_JSON)
	public String consultarItinerarios() {
		
		JSONArray itinerarios = manager.getDAOPontos().consultarItinerarios();
		return itinerarios.toString();
	}
	
	@GET
	@Path("horarios")
	@Produces(MediaType.APPLICATION_JSON)
	public String consultarHorarios() {
		
		JSONArray horarios = manager.getDAOPontos().consultarHorarios();
		return horarios.toString();
	}

	@GET
	@Path("problemas_onibus")
	@Produces(MediaType.APPLICATION_JSON)
	public String consultarProblemasOnibus() {
		
		JSONArray problemasOnibus = manager.getDAOPontos().consultarProblemasOnibusCrowdsourcing();
		return problemasOnibus.toString();
	}
	
	@GET
	@Path("problemas_linhas")
	@Produces(MediaType.APPLICATION_JSON)
	public String consultarProblemasLinhas() {

		JSONArray problemasLinhas = manager.getDAOPontos().consultarProblemasLinhasCrowdsourcing();
		return problemasLinhas.toString();
	}
}
