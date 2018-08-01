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
import com.tcc2.database.DAOPontos;

@Path("pontos")
public class WSPontos {
	
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getIt() {
        return "Got it 222!";
    }

    @GET
    @Path("teste")
    @Produces(MediaType.APPLICATION_JSON)
    public Response testeJson() {
        
    	JSONArray array = new JSONArray();
    	array.put(true);
    	array.put(1234);
    	array.put("testetestetes");
    	
    	JSONObject json = new JSONObject();
    	json.put("id", 1);
    	json.put("nome", "brendon");
    	json.put("lista", array);
    	
    	return Response.ok(json.toString()).build();
    }
    
	@GET
	@Path("pontosproximos")
	@Produces(MediaType.APPLICATION_JSON)	
	public Response consultarPontosProximos(@NotNull @QueryParam("latitude") double latitude, 
											@NotNull @QueryParam("longitude") double longitude) {
				
        DAOPontos dao = new DAOPontos();
        List<PontoDeOnibus> lista = dao.consultarPontosDeOnibusProximos(latitude, longitude);
        
		return Response.ok(lista).build();
	}
	
	@GET
	@Path("pontos")
	@Produces(MediaType.APPLICATION_JSON)
	public String consultarPontos() {
		
		DAOPontos dao = new DAOPontos();
		JSONArray pontos = dao.consultarPontosDeOnibus();
		
		return pontos.toString();
	}
	
	@GET
	@Path("tipospontos")
	@Produces(MediaType.APPLICATION_JSON)
	public String consultarTiposDePontos() {
		
		DAOPontos dao = new DAOPontos();
		JSONArray tipos = dao.consultarTiposDePonto();
		
		return tipos.toString();
	}
	
	@GET
	@Path("bairros")
	@Produces(MediaType.APPLICATION_JSON)
	public String consultarBairros() {
		
		DAOPontos dao = new DAOPontos();
		JSONArray bairros = dao.consultarBairros();
		
		return bairros.toString();
	}
	
	@GET
	@Path("linhas")
	@Produces(MediaType.APPLICATION_JSON)
	public String consultarLinhas() {
		
		DAOPontos dao = new DAOPontos();
		JSONArray linhas = dao.consultarLinhas();
		
		return linhas.toString();
	}
	
	
	@GET
	@Path("categorias")
	@Produces(MediaType.APPLICATION_JSON)
	public String consultarCategoriaDeLinha() {
		
		DAOPontos dao = new DAOPontos();
		JSONArray categorias = dao.consultarCategoriasDeLinhas();
		
		return categorias.toString();
	}
	

	@GET
	@Path("itinerarios")
	@Produces(MediaType.APPLICATION_JSON)
	public String consultarItinerarios() {
		
		DAOPontos dao = new DAOPontos();
		JSONArray itinerarios = dao.consultarItinerarios();
		
		return itinerarios.toString();
	}
	
	@GET
	@Path("horarios")
	@Produces(MediaType.APPLICATION_JSON)
	public String consultarHorarios() {
		DAOPontos dao = new DAOPontos();
		JSONArray horarios = dao.consultarHorarios();
		
		return horarios.toString();
	}

}
