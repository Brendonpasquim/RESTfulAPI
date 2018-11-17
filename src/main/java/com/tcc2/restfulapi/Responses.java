package com.tcc2.restfulapi;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import br.com.starmetal.validacoes.Validacao;

public class Responses {
	
	public static Response getSucess(String mensagem) {
		if(Validacao.ehStringVazia(mensagem)) {
			return Response.status(Status.OK).build();
		}
		
		return Response.status(Status.OK).entity(mensagem).build();
	}
	
	public static Response getEmpty(String mensagem) {
		if(Validacao.ehStringVazia(mensagem)) {
			return Response.status(Status.NO_CONTENT).build();
		}
		
		return Response.status(Status.NO_CONTENT).entity(mensagem).build();		
	}
	
	public static Response getError(String mensagem) {
		if(Validacao.ehStringVazia(mensagem)) {
			return Response.status(Status.BAD_REQUEST).build();
		}
		
		return Response.status(Status.BAD_REQUEST).entity(mensagem).build();
	}

}
