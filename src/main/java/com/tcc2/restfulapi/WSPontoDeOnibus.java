package com.tcc2.restfulapi;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

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
	public List<PontoDeOnibus> consultarPontosProximos(@QueryParam("latitude_ponto") double latitude, 
										  			   @QueryParam("longitude_ponto") double longitude) {
				
		System.out.println(System.getProperty( "catalina.base" ));
		
        DAOPontoOnibus dao = new DAOPontoOnibus();
        List<PontoDeOnibus> lista = dao.consultarPontosDeOnibus("dummy");
		
		return lista;
	}

}
