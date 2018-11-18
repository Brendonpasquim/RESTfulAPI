package com.tcc2.restfulapi;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.json.JSONArray;

import br.com.starmetal.results.ResultType;
import br.com.starmetal.validacoes.Validacao;

public class Responses {
	
	public static Response getSucess(String mensagem) {
		if(Validacao.ehStringVazia(mensagem)) {
			return Response.ok().build();
		}
		
		return Response.ok().entity(mensagem).build();
	}
	
	public static Response getEmpty() {
		return Response.noContent().build();		
	}
	
	public static Response getError(String mensagem) {
		if(Validacao.ehStringVazia(mensagem)) {
			return Response.status(Status.BAD_REQUEST).build();
		}
		
		return Response.status(Status.BAD_REQUEST).entity(mensagem).build();
	}
	
	public static Response getStatusGET(JSONArray array) {
		if(array == null || array.length() == 0) {
			return Response.noContent().build();
		} else {
			return Response.ok().entity(array.toString()).build();
		}
	}
	
	public static Response getStatusPOST(ResultType result) {
		if(result.equals(ResultType.SUCESS)) {
			return getSucess("Cadastro efetuado");
		} else {
			return getError("Falha ao realizar cadastro.");
		}
	}
}
