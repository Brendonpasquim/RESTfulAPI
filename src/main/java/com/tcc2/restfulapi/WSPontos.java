package com.tcc2.restfulapi;

import static com.tcc2.restfulapi.Responses.getStatusGET;

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
	private DAOManager manager;
	
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
	@Path("pontos_proximos_simplificado")
	@Produces(MediaType.APPLICATION_JSON)	
	public Response consultarPontosProximosSimplificado(@NotNull @QueryParam("latitude") double latitude, 
													  @NotNull @QueryParam("longitude") double longitude) {
		
        JSONArray pontosProximosSimplificados = manager.getDAOPontos().consultarPontosDeOnibusProximosSimplificado(latitude, longitude);
		return getStatusGET(pontosProximosSimplificados);
	}	
	
	@GET
	@Path("pontos")
	@Produces(MediaType.APPLICATION_JSON)
	public Response consultarPontos(@QueryParam("bairro") String bairro) {
		
		JSONArray pontos = manager.getDAOPontos().consultarPontosDeOnibus(bairro);
		return getStatusGET(pontos);
	}
	
	@GET
	@Path("tipos_pontos")
	@Produces(MediaType.APPLICATION_JSON)
	public Response consultarTiposDePontos() {
		
		JSONArray tipos = manager.getDAOPontos().consultarTiposDePonto();
		return getStatusGET(tipos);
	}
	
	@GET
	@Path("bairros")
	@Produces(MediaType.APPLICATION_JSON)
	public Response consultarBairros() {
		
		JSONArray bairros = manager.getDAOPontos().consultarBairros();
		return getStatusGET(bairros);
	}
	
	@GET
	@Path("linhas")
	@Produces(MediaType.APPLICATION_JSON)
	public Response consultarLinhas() {
		
		JSONArray linhas = manager.getDAOPontos().consultarLinhas();
		return getStatusGET(linhas);
	}
	
	
	@GET
	@Path("categorias")
	@Produces(MediaType.APPLICATION_JSON)
	public Response consultarCategoriasDeLinhas() {
		
		JSONArray categorias = manager.getDAOPontos().consultarCategoriasDeLinhas();
		return getStatusGET(categorias);
	}
	

	@GET
	@Path("itinerarios")
	@Produces(MediaType.APPLICATION_JSON)
	public Response consultarItinerarios() {
		
		JSONArray itinerarios = manager.getDAOPontos().consultarItinerarios();
		return getStatusGET(itinerarios);
	}
	
	@GET
	@Path("horarios")
	@Produces(MediaType.APPLICATION_JSON)
	public Response consultarHorarios() {
		
		JSONArray horarios = manager.getDAOPontos().consultarHorarios();
		return getStatusGET(horarios);
	}

	@GET
	@Path("problemas_onibus")
	@Produces(MediaType.APPLICATION_JSON)
	public Response consultarProblemasOnibus(@QueryParam("bairro") String bairro) {
		
		JSONArray problemasOnibus = manager.getDAOPontos().consultarProblemasOnibusCrowdsourcing(bairro);
		return getStatusGET(problemasOnibus);
	}
	
	@GET
	@Path("problemas_linhas")
	@Produces(MediaType.APPLICATION_JSON)
	public Response consultarProblemasLinhas(@QueryParam("bairro") String bairro) {

		JSONArray problemasLinhas = manager.getDAOPontos().consultarProblemasLinhasCrowdsourcing(bairro);
		return getStatusGET(problemasLinhas);
	}
}
