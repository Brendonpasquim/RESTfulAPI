package com.tcc2.database;

import java.util.logging.Logger;

import org.json.JSONArray;

import br.com.starmetal.database.postgresql.QueryMaker;

public class DAORotas {

	private DAOPontos daoPontos;
	private Executor executar;
	private static final Logger LOG = Logger.getLogger(DAORotas.class.getName());
	
	public DAORotas(DAOManager manager) {
		this.daoPontos = manager.getDAOPontos(); 
		this.executar = manager.getQueryExecutor();
	}
	
	/**
	 * Faz a consulta por rotas entre dois pontos, considerando todos os pontos próximos
	 * de uma dada origem e um dado destino.
	 * 
	 * @param latitudeOrigem
	 * @param longitudeOrigen
	 * @param latitudeDestino
	 * @param longitudeDestino
	 * @return
	 */
	public JSONArray consultarRotaSimplesEntrePontosProximos(double latitudeOrigem, double longitudeOrigen, double latitudeDestino, double longitudeDestino) {
		if(latitudeOrigem < 0 || longitudeOrigen < 0 || latitudeDestino < 0 || longitudeDestino < 0) {
			String mensagemValidacao = "Parâmetros inválidos fornecidos no método 'consultarRotaSimplesEntrePontosProximos'. Valores fornecidos são: '%f' '%f' '%f' '%f'"; 
			LOG.warning(String.format(mensagemValidacao, latitudeOrigem, longitudeOrigen, latitudeDestino, longitudeDestino));
			return new JSONArray();
		}
		
		JSONArray listaPontosProximosOrigem = daoPontos.consultarPontosDeOnibusProximosSimplificado(latitudeOrigem, longitudeOrigen);
		JSONArray listaPontosProximosDestino = daoPontos.consultarPontosDeOnibusProximosSimplificado(latitudeDestino, longitudeDestino);
		
		int numeroPontoOrigem;
		int numeroPontoDestino;
		JSONArray rota = new JSONArray();
		for(int indiceOrigem = 0; indiceOrigem < listaPontosProximosOrigem.length(); indiceOrigem++) {
			numeroPontoOrigem = listaPontosProximosOrigem.getJSONObject(indiceOrigem).getInt("numero_ponto");
			
			for(int indiceDestino = 0; indiceDestino < listaPontosProximosDestino.length(); indiceDestino++) {
				numeroPontoDestino = listaPontosProximosDestino.getJSONObject(indiceDestino).getInt("numero_ponto");
				
				rota = procurarRotaSimples(numeroPontoOrigem, numeroPontoDestino);
				
				if(rota.length() > 0) {
					JSONArray temp;
					String codigoLinha;
					
					for(int indiceRota = 0; indiceRota < rota.length(); indiceRota++) {
						temp = procurarPonto(numeroPontoOrigem);
						rota.getJSONObject(indiceRota).put("dados_ponto_origem", temp.getJSONObject(0));
						
						temp = procurarPonto(numeroPontoDestino);
						rota.getJSONObject(indiceRota).put("dados_ponto_destino", temp.getJSONObject(0));
						
						codigoLinha = rota.getJSONObject(indiceRota).getString("codigo_linha");
						
						temp = procurarLinha(codigoLinha);
						rota.getJSONObject(indiceRota).put("dados_linha", temp.getJSONObject(0));
						
						temp = consultarInfoRotaSimples(numeroPontoOrigem, numeroPontoDestino, codigoLinha);
						rota.getJSONObject(indiceRota).put("info_rota", temp);
					}
				}
			}
		}
		
		return rota;
	}

	/**
	 * Faz a consulta por rotas entre dois pontos, considerando especificamente os ponto de origem
	 * e de destino fornecido por parâmetro.
	 * 
	 * @param numeroPontoOrigem
	 * @param numeroPontoDestino
	 * @return
	 */
	public JSONArray consultarRotaSimplesEntreDoisPontos(int numeroPontoOrigem, int numeroPontoDestino) {
		if(numeroPontoOrigem < 0 || numeroPontoDestino < 0) {
			String mensagemValidacao = "Parâmetros inválidos fornecidos no método 'consultarRotaSimplesEntreDoisPontos'. Valores fornecidos são: '%d' '%d'"; 
			LOG.warning(String.format(mensagemValidacao, numeroPontoOrigem, numeroPontoDestino));
			return new JSONArray();
		}
		
		JSONArray rota = procurarRotaSimples(numeroPontoOrigem, numeroPontoDestino);
		
		if(rota.length() > 0) {
			JSONArray temp;
			String codigoLinha;
			
			for(int indiceRota = 0; indiceRota < rota.length(); indiceRota++) {
				temp = procurarPonto(numeroPontoOrigem);
				rota.getJSONObject(indiceRota).put("dados_ponto_origem", temp.getJSONObject(0));
				
				temp = procurarPonto(numeroPontoDestino);
				rota.getJSONObject(indiceRota).put("dados_ponto_destino", temp.getJSONObject(0));
				
				codigoLinha = rota.getJSONObject(indiceRota).getString("codigo_linha");
				
				temp = procurarLinha(codigoLinha);
				rota.getJSONObject(indiceRota).put("dados_linha", temp.getJSONObject(0));
				
				temp = consultarInfoRotaSimples(numeroPontoOrigem, numeroPontoDestino, codigoLinha);
				rota.getJSONObject(indiceRota).put("info_rota", temp);
			}
		}
		
		return rota;
	}
	
	public JSONArray consultarRotaConectada() {
		return new JSONArray();
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
		
		return executar.queryExecutor(query);
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
		
		return executar.queryExecutor(query);
	}
	
	//========================= FUNÇÕES AUXILIARES =========================
	
	public JSONArray procurarPonto(int numeroPonto) {
		QueryMaker queryPrincipal = new QueryMaker();
		queryPrincipal.select("numero_ponto", "endereco", "tipo, ST_AsGeoJSON(geom, 15, 0) as geojson")
					  .from("pontos_de_onibus")
					  .where("numero_ponto ", numeroPonto);
		
		QueryMaker querySecundaria = new QueryMaker();
		querySecundaria.select("A.tipo", "COUNT(A.tipo) as count_tipos", "B.peso")
					   .from("crowdsourcing_pontos A, crowdsourcing_regras B")
					   .where("A.tipo = B.tipo")
					   .where("A.numero_ponto", numeroPonto)
					   .groupBy("A.tipo", "B.peso");
		
		JSONArray principal = executar.queryExecutor(queryPrincipal);
		JSONArray secundaria = executar.queryExecutor(querySecundaria);
		
		//Acrescenta o resultado da segunda query ao JSON resultante da primeira query.
		principal.getJSONObject(0).put("ocorrencias", secundaria);
		
		return principal;
	}
	
	public JSONArray procurarLinha(String codigoLinha) {
		QueryMaker queryPrincipal = new QueryMaker();
		queryPrincipal.select("codigo_linha", "nome_linha", "cor", "categoria", "apenas_cartao")
					  .from("linhas_de_onibus")
					  .where("codigo_linha", codigoLinha);
		
		QueryMaker querySecundaria = new QueryMaker();
		querySecundaria.select("B.tipo", "COUNT(B.tipo) as count_tipos", "B.peso")
					   .from("crowdsourcing_linhas A, crowdsourcing_regras B")
					   .where("A.tipo = B.tipo")
					   .where("A.codigo_linha", codigoLinha)
					   .groupBy("B.tipo", "B.peso");
		
		JSONArray principal = executar.queryExecutor(queryPrincipal);
		JSONArray secundaria = executar.queryExecutor(querySecundaria);
		
		//Acrescenta o resultado da segunda query ao JSON resultante da primeira query.
		principal.getJSONObject(0).put("ocorrencias", secundaria);
		
		return principal;
	}
	
	public JSONArray procurarRotaSimples(int numeroPontoOrigem, int numeroPontoDestino) {
		QueryMaker query = new QueryMaker();
		query.select("DISTINCT A.codigo_linha", "A.direcao")
			 .from("pontos_de_onibus A, pontos_de_onibus B")
			 .where("A.numero_ponto", numeroPontoOrigem)
			 .where("B.numero_ponto", numeroPontoDestino)
			 .where("A.codigo_linha = B.codigo_linha")
			 .where("A.seq < B.seq")
			 .where("A.direcao = B.direcao");
		
		return executar.queryExecutor(query);
	}
	
	public JSONArray consultarInfoRotaSimples(int numeroPontoOrigem, int numeroPontoDestino, String codigoLinha) {
		
		String subQuery = "(A.seq >= (SELECT MIN(seq) FROM pontos_de_onibus WHERE codigo_linha = A.codigo_linha AND numero_ponto = ':X' AND direcao = A.direcao) " + 
		 				  "AND A.seq <= (SELECT MAX(seq) FROM pontos_de_onibus WHERE codigo_linha = A.codigo_linha AND numero_ponto = ':Y' AND direcao = A.direcao))";
		
		QueryMaker query = new QueryMaker();
		query.select("A.seq", "A.numero_ponto", "A.codigo_linha", "ST_AsGeoJSON(geom, 15, 0) geojson")
			 .from("pontos_de_onibus A")
			 .where(subQuery).setParameter("X", numeroPontoOrigem).setParameter("Y", numeroPontoDestino)
			 .where("A.codigo_linha", codigoLinha)
			 .orderBy("codigo_linha, seq");
		
		return executar.queryExecutor(query);
	}
	
	public JSONArray procurarTerminalOrigem(int numeroPonto) {
		QueryMaker query = new QueryMaker();
		query.select("DISTINCT B.endereco", "B.numero_ponto")
			 .from("pontos_de_onibus A, pontos_de_onibus B")
			 .where("A.numero_ponto", numeroPonto)
			 .where("A.codigo_linha = B.codigo_linha")
			 .where("A.seq < B.seq")
			 .where("A.direcao = B.direcao")
			 .where("(position('SITES' in B.endereco) = 1 OR position('Terminal' in B.endereco) = 1")
			 .where("(B.tipo = 'Plataforma' OR B.tipo = 'Estação tubo') OR position('Estação Tubo' in B.endereco) = 1)");
		
		return executar.queryExecutor(query);
	}
	
	public JSONArray procurarTerminalDestino(int numeroPonto) {
		QueryMaker query = new QueryMaker();
		query.select("DISTINCT A.endereco, A.numero_ponto")
			 .from("pontos_de_onibus A, pontos_de_onibus B")
			 .where("B.numero_ponto", numeroPonto)
			 .where("A.codigo_linha = B.codigo_linha")
			 .where("A.seq < B.seq")
			 .where("(position('SITES' in A.endereco) = 1 OR position('Terminal' in A.endereco) = 1")
			 .where("(A.tipo = 'Plataforma' OR A.tipo = 'Estação tubo') OR position('Estação Tubo' in A.endereco) = 1)");
		
		return executar.queryExecutor(query);
	}
}