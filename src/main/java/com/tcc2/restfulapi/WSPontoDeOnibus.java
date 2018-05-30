package com.tcc2.restfulapi;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.tcc2.beans.PontoDeOnibus;
import com.tcc2.database.DAOPontoOnibus;

@Path("ponto")
public class WSPontoDeOnibus {
	
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
				
        DAOPontoOnibus dao = new DAOPontoOnibus();
        List<PontoDeOnibus> lista = dao.consultarPontosDeOnibusProximos(latitude, longitude);
        
		return Response.ok(lista).build();
	}

}
