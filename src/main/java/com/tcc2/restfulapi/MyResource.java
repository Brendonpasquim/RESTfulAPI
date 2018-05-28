package com.tcc2.restfulapi;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("myresource")
public class MyResource {

    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getIt() {
        return "Got it!";
    }
    
    @GET
    @Path("soma/{a}/{b}")
    @Produces(MediaType.TEXT_PLAIN)
    public String soma(@PathParam("a") int a, @PathParam("b") int b) {
    	return String.valueOf(a + b);
    }
    
    @PUT
    @Path("enviar/{registro}")
    @Produces(MediaType.TEXT_PLAIN)
    public String criarRegistro(@PathParam("registro") String registro) {
    	return String.format("Registro '%s' criado com sucesso.", registro);
    }
    
    @POST
    @Path("enviar/{registro}")
    @Produces(MediaType.TEXT_PLAIN)
    public String atualizarRegistro(@PathParam("registro") String registro) {
    	return String.format("Registro '%s' atualizado com sucesso.", registro);
    }
    
    @PUT
    @Path("/enviar")
    @Produces(MediaType.TEXT_PLAIN)
    public String criarRegistroQuery(@DefaultValue("naoPreenchido") @QueryParam("registro") String registro) {
    	return String.format("Registro '%s' criado com sucesso.", registro);
    }
    
    @PUT
    @Path("/enviar")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.TEXT_PLAIN)
    public String criarRegistroCorpo(String registro) {
    	return String.format("Registro '%s' criado com sucesso. CODE 111", registro);
    }
    
    @POST
    @Path("enviar")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.TEXT_PLAIN)
    public String atualizarRegistroCorpo(String registro) {
    	return String.format("Registro '%s' atualizado com sucesso. CODE 222", registro);
    }
}
