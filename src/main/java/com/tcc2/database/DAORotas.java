package com.tcc2.database;

import org.json.JSONArray;

import br.com.starmetal.database.postgresql.QueryMaker;

public class DAORotas {

	private DAOPontos daoPontos;
	private QueryExecutor executar;

	public DAORotas(DAOManager manager) {
		this.daoPontos = manager.getDAOPontos(); 
		this.executar = manager.getQueryExecutor();
	}
	
	public JSONArray consultarRotaSimples(double latitudeOrigem, double longitudeOrigen, double latitudeDestino, double longitudeDestino) {
		JSONArray listaPontosProximosOrigem = daoPontos.consultarPontosDeOnibusProximosSimplificado(latitudeOrigem, longitudeOrigen);
		JSONArray listaPontosProximosDestino = daoPontos.consultarPontosDeOnibusProximosSimplificado(latitudeDestino, longitudeDestino);
		
		int numeroPontoX;
		int numeroPontoY;
		JSONArray rota = new JSONArray();
		for(int x = 0; x < listaPontosProximosOrigem.length(); x++) {
			numeroPontoX = listaPontosProximosOrigem.getJSONObject(x).getInt("numero_ponto");
			
			for(int y = 0; y < listaPontosProximosDestino.length(); y++) {
				numeroPontoY = listaPontosProximosDestino.getJSONObject(y).getInt("numero_ponto");
				
				rota = procurarRotaSimples(x, y);
				
				if(rota.length() > 0) {
					JSONArray temp;
					String codigoLinha;
					
					for(int indiceRota = 0; indiceRota < rota.length(); indiceRota++) {
						temp = procurarPonto(numeroPontoX);
						rota.getJSONObject(indiceRota).put("dados_ponto_origem", temp.getJSONObject(0));
						
						temp = procurarPonto(numeroPontoY);
						rota.getJSONObject(indiceRota).put("dados_ponto_destino", temp.getJSONObject(0));
						
						codigoLinha = rota.getJSONObject(indiceRota).getString("codigo_linha");
						
						temp = procurarLinha(codigoLinha);
						rota.getJSONObject(indiceRota).put("dados_linha", temp.getJSONObject(0));
					}
				}
			}
		}
		
		return rota;
	}
	
	/**
	 * 
	 * @return
	 */
	public JSONArray consultarOrigem() {
		QueryMaker query = new QueryMaker();
		query.select("R.data_viagem", "R.horario_saida", "R.codigo_linha", "L.nome_linha", "L.cor", "L.apenas_cartao", "R.ponto_saida", "P.endereco", "P.tipo, ST_AsGeoJSON(P.geom, 15, 0) as geojson")
			 .from("relatorio_viagem R, pontos_de_onibus P, linhas_de_onibus L, divisa_de_bairros B")
			 .where("ST_Within(P.geom, ST_Transform(ST_setSRID(B.geom, 29192), 4326))")
			 .where("R.codigo_linha = L.codigo_linha")
			 .where("R.numero_ponto = P.numero_ponto");
		
		return executar.QueryExecutor(query);
	}
	
	/**
	 * 
	 * @return
	 */
	public JSONArray consultarDestino() {
		QueryMaker query = new QueryMaker();
		query.select("R.data_viagem", "R.horario_chegada", "R.codigo_linha", "L.nome_linha", "L.cor", "L.apenas_cartao", "R.ponto_chegada", "P.endereco", "P.tipo", "ST_AsGeoJSON(P.geom, 15, 0) as geojson")
			 .from("relatorio_viagem R, pontos_de_onibus P, linhas_de_onibus L, divisa_de_bairros B")
			 .where("ST_Within(P.geom, ST_Transform(ST_setSRID(B.geom, 29192), 4326))")
			 .where("R.codigo_linha = L.codigo_linha")
			 .where("R.numero_ponto = P.numero_ponto");
		
		return executar.QueryExecutor(query);
	}
	
	//========================= FUNÇÕES AUXILIARES =========================
	
	public JSONArray procurarPonto(int x) {
		QueryMaker queryPrincipal = new QueryMaker();
		queryPrincipal.select("numero_ponto", "endereco", "tipo, ST_AsGeoJSON(geom, 15, 0) as geojson")
					  .from("pontos_de_onibus ")
					  .where("numero_ponto ", x);
		
		QueryMaker querySecundaria = new QueryMaker();
		querySecundaria.select("tipo", "COUNT(tipo) as count_tipos", "peso")
					   .from("crowdsourcing_linhas A, crowdsourcing_regras B")
					   .where("A.tipo = B.tipo")
					   .where("A.numero_ponto", String.valueOf(x))
					   .groupBy("A.tipo", "A.peso");
		
		JSONArray principal = executar.QueryExecutor(queryPrincipal);
		JSONArray secundaria = executar.QueryExecutor(querySecundaria);
		
		//Acrescenta o resultado da segunda query ao JSON resultante da primeira query.
		principal.getJSONObject(0).put("ocorrencias", secundaria);
		
		return principal;
	}
	
	public JSONArray procurarLinha(String codigoLinha) {
		QueryMaker queryPrincipal = new QueryMaker();
		queryPrincipal.select("codigo_linha", "nome_linha", "cor", "categoria", "apenas_cartao", "adaptado")
					  .from("linhas_de_onibus")
					  .where("codigo_linha", codigoLinha);
		
		QueryMaker querySecundaria = new QueryMaker();
		querySecundaria.select("tipo", "COUNT(tipo) as count_tipos", "peso")
					   .from("crowdsourcing_linhas A, crowdsourcing_regras B")
					   .where("A.tipo = B.tipo")
					   .where("A.codigo_linha", codigoLinha)
					   .groupBy("A.tipo", "A.peso");
		
		JSONArray principal = executar.QueryExecutor(queryPrincipal);
		JSONArray secundaria = executar.QueryExecutor(querySecundaria);
		
		//Acrescenta o resultado da segunda query ao JSON resultante da primeira query.
		principal.getJSONObject(0).put("ocorrencias", secundaria);
		
		return principal;
	}
	
	public JSONArray procurarRotaSimples(int x, int y) {
		QueryMaker withStatement = new QueryMaker();
		withStatement.select("DISTINCT A.codigo_linha", "A.direcao")
					 .from("pontos_de_onibus A, pontos_de_onibus B")
					 .where("A.numero_ponto", x)
					 .where("B.numero_ponto", y)
					 .where("A.codigo_linha = B.codigo_linha")
					 .where("A.seq < B.seq")
					 .where("A.direcao = B.direcao");
		
		QueryMaker query = new QueryMaker();
		query.with(withStatement, "linha")
			 .select("A.seq", "A.numero_ponto", "A.codigo_linha", "ST_AsGeoJSON(geom, 15, 0) geojson")
			 .from("pontos_de_onibus A")
			 .where("(A.seq >= (SELECT MIN(seq) FROM pontos_de_onibus WHERE codigo_linha = A.codigo_linha AND numero_ponto = 'X' AND direcao = A.direcao) " + 
			 		"AND A.seq <= (SELECT MAX(seq) FROM pontos_de_onibus WHERE codigo_linha = A.codigo_linha AND numero_ponto = 'Y' AND direcao = A.direcao))")
			 .where("A.codigo_linha IN (SELECT A.codigo_linha FROM linha WHERE direcao = A.direcao)")
			 .orderBy("codigo_linha, seq");
		
		return executar.QueryExecutor(query);
	}
	
	public JSONArray procurarTerminalOrigem(int x) {
		QueryMaker query = new QueryMaker();
		query.select("DISTINCT B.endereco", "B.numero_ponto")
			 .from("pontos_de_onibus A, pontos_de_onibus B")
			 .where("A.numero_ponto", x)
			 .where("A.codigo_linha = B.codigo_linha")
			 .where("A.seq < B.seq")
			 .where("A.direcao = B.direcao")
			 .where("(position('SITES' in B.endereco) = 1 OR position('Terminal' in B.endereco) = 1")
			 .where("(B.tipo = 'Plataforma' OR B.tipo = 'Estação tubo') OR position('Estação Tubo' in B.endereco) = 1)");
		
		return executar.QueryExecutor(query);
	}
	
	public JSONArray procurarTerminalDestino(int y) {
		QueryMaker query = new QueryMaker();
		query.select("DISTINCT A.endereco, A.numero_ponto")
			 .from("pontos_de_onibus A, pontos_de_onibus B")
			 .where("B.numero_ponto", y)
			 .where("A.codigo_linha = B.codigo_linha")
			 .where("A.seq < B.seq")
			 .where("(position('SITES' in A.endereco) = 1 OR position('Terminal' in A.endereco) = 1")
			 .where("(A.tipo = 'Plataforma' OR A.tipo = 'Estação tubo') OR position('Estação Tubo' in A.endereco) = 1)");
		
		return executar.QueryExecutor(query);
	}
}