package com.tcc2.restfulapi;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import com.tcc2.beans.PontoDeOnibus;
import com.tcc2.database.DAOBaseUTFPR;
import com.tcc2.database.DAOPontos;

@Path("pontos")
public class WSPontos {
	
	private DAOPontos dao;
	
	public WSPontos() {
		dao = new DAOPontos();
	}
	
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getIt() {
        return "Got it 222!";
    }

	@GET
	@Path("pontosproximos")
	@Produces(MediaType.APPLICATION_JSON)	
	public Response consultarPontosProximos(@NotNull @QueryParam("latitude") double latitude, 
											@NotNull @QueryParam("longitude") double longitude) {
				
        List<PontoDeOnibus> lista = dao.consultarPontosDeOnibusProximos(latitude, longitude);
		return Response.ok(lista).build();
	}
	
	@GET
	@Path("pontos")
	@Produces(MediaType.APPLICATION_JSON)
	public String consultarPontos() {
		
		JSONArray pontos = dao.consultarPontosDeOnibus();
		return pontos.toString();
	}
	
	@GET
	@Path("tipospontos")
	@Produces(MediaType.APPLICATION_JSON)
	public String consultarTiposDePontos() {
		
		JSONArray tipos = dao.consultarTiposDePonto();
		return tipos.toString();
	}
	
	@GET
	@Path("bairros")
	@Produces(MediaType.APPLICATION_JSON)
	public String consultarBairros() {
		
		JSONArray bairros = dao.consultarBairros();
		return bairros.toString();
	}
	
	@GET
	@Path("linhas")
	@Produces(MediaType.APPLICATION_JSON)
	public String consultarLinhas() {
		
		JSONArray linhas = dao.consultarLinhas();
		return linhas.toString();
	}
	
	
	@GET
	@Path("categorias")
	@Produces(MediaType.APPLICATION_JSON)
	public String consultarCategoriaDeLinha() {
		
		JSONArray categorias = dao.consultarCategoriasDeLinhas();
		return categorias.toString();
	}
	

	@GET
	@Path("itinerarios")
	@Produces(MediaType.APPLICATION_JSON)
	public String consultarItinerarios() {
		
		JSONArray itinerarios = dao.consultarItinerarios();
		return itinerarios.toString();
	}
	
	@GET
	@Path("horarios")
	@Produces(MediaType.APPLICATION_JSON)
	public String consultarHorarios() {
		
		JSONArray horarios = dao.consultarHorarios();
		return horarios.toString();
	}

}
