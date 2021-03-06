package com.tcc2.database;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONObject;

import br.com.starmetal.database.postgresql.QueryMaker;
import br.com.starmetal.validacoes.Validacao;

public class DAORotas {

	private DAOPontos daoPontos;
	private Executor executar;
	private static final Logger LOG = Logger.getLogger(DAORotas.class.getName());

	public DAORotas(DAOManager manager) {
		this.daoPontos = manager.getDAOPontos();
		this.executar = manager.getExecutor();
	}

	/**
	 * Faz a consulta por rotas entre dois pontos, considerando todos os pontos
	 * próximos de uma dada origem e um dado destino.
	 * 
	 * @param latitudeOrigem
	 * @param longitudeOrigen
	 * @param latitudeDestino
	 * @param longitudeDestino
	 * @return
	 */
	public JSONArray consultarRotaSimplesEntrePontosProximos(double latitudeOrigem, double longitudeOrigen,	double latitudeDestino, double longitudeDestino) {
		JSONArray listaPontosProximosOrigem = daoPontos.consultarPontosDeOnibusProximosSimplificado(latitudeOrigem,	longitudeOrigen);
		JSONArray listaPontosProximosDestino = daoPontos.consultarPontosDeOnibusProximosSimplificado(latitudeDestino, longitudeDestino);

		int numeroPontoOrigem;
		int numeroPontoDestino;
		JSONArray rota = new JSONArray();
		for (int indiceOrigem = 0; indiceOrigem < listaPontosProximosOrigem.length(); indiceOrigem++) {
			numeroPontoOrigem = listaPontosProximosOrigem.getJSONObject(indiceOrigem).getInt("numero_ponto");

			for (int indiceDestino = 0; indiceDestino < listaPontosProximosDestino.length(); indiceDestino++) {
				numeroPontoDestino = listaPontosProximosDestino.getJSONObject(indiceDestino).getInt("numero_ponto");

				rota = procurarRotaSimples(numeroPontoOrigem, numeroPontoDestino);

				if (rota.length() > 0) {
					JSONArray temp;
					String codigoLinha;

					for (int indiceRota = 0; indiceRota < rota.length(); indiceRota++) {
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
	 * Faz a consulta por rotas entre dois pontos, considerando especificamente os
	 * ponto de origem e de destino fornecido por parâmetro.
	 * 
	 * @param numeroPontoOrigem
	 * @param numeroPontoDestino
	 * @return
	 */
	public JSONArray consultarRotaSimplesEntreDoisPontos(int numeroPontoOrigem, int numeroPontoDestino) {
		if (numeroPontoOrigem < 0 || numeroPontoDestino < 0) {
			String mensagemValidacao = "Parâmetros inválidos fornecidos no método 'consultarRotaSimplesEntreDoisPontos'. Valores fornecidos são: '%d' '%d'";
			LOG.warning(String.format(mensagemValidacao, numeroPontoOrigem, numeroPontoDestino));
			return new JSONArray();
		}

		JSONArray rota = procurarRotaSimples(numeroPontoOrigem, numeroPontoDestino);

		if (rota.length() > 0) {
			JSONArray temp;
			String codigoLinha;

			for (int indiceRota = 0; indiceRota < rota.length(); indiceRota++) {
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

	public JSONArray consultarRotaConectada(int numeroPontoOrigem, int numeroPontoDestino) {

		JSONArray rotaConectada = new JSONArray();
		JSONArray rotasOrigemDesembarque = new JSONArray();
		JSONArray rotasEmbarqueDestino = new JSONArray();
		JSONArray tempRotaOrigemDesembarque;
		JSONArray tempRotaEmbarqueDestino;

		JSONObject dadosRotas = new JSONObject();
		JSONArray temp;
		String codigoLinha;

		JSONArray terminaisOrigem = procurarTerminalOrigem(numeroPontoOrigem);
		JSONArray terminaisDestino = procurarTerminalDestino(numeroPontoDestino);
		if (terminaisOrigem.length() == 0 || terminaisDestino.length() == 0) {
			return new JSONArray();
		}

		HashMap<String, JSONObject> mapTerminaisOrigem = JSONArrayTerminaisToHashMap(terminaisOrigem);
		HashMap<String, JSONObject> mapTerminaisDestino = JSONArrayTerminaisToHashMap(terminaisDestino);

		for (Map.Entry<String, JSONObject> terminalOrigem : mapTerminaisOrigem.entrySet()) {
			if (mapTerminaisDestino.containsKey(terminalOrigem.getKey())) {
				int numeroPontoDesembarque = terminalOrigem.getValue().getInt("numero_ponto");
				int numeroPontoEmbarque = mapTerminaisDestino.get(terminalOrigem.getKey()).getInt("numero_ponto");

				tempRotaOrigemDesembarque = procurarRotaSimples(numeroPontoOrigem, numeroPontoDesembarque);
				if (tempRotaOrigemDesembarque.length() > 0) {
					for (int indice = 0; indice < tempRotaOrigemDesembarque.length(); indice++) {
						temp = procurarPonto(numeroPontoOrigem);
						tempRotaOrigemDesembarque.getJSONObject(indice).put("dados_ponto_origem", temp.getJSONObject(0));

						temp = procurarPonto(numeroPontoDesembarque);
						tempRotaOrigemDesembarque.getJSONObject(indice).put("dados_ponto_desembarque", temp.getJSONObject(0));

						codigoLinha = tempRotaOrigemDesembarque.getJSONObject(indice).getString("codigo_linha");
						temp = procurarLinha(codigoLinha);
						tempRotaOrigemDesembarque.getJSONObject(indice).put("dados_primeira_linha", temp.getJSONObject(0));

						temp = consultarInfoRotaSimples(numeroPontoOrigem, numeroPontoDesembarque, codigoLinha);
						tempRotaOrigemDesembarque.getJSONObject(indice).put("info_rota_primeira", temp);
					}
				}

				tempRotaEmbarqueDestino = procurarRotaSimples(numeroPontoEmbarque, numeroPontoDestino);
				if (tempRotaEmbarqueDestino.length() > 0) {
					for(int indice = 0; indice < tempRotaEmbarqueDestino.length(); indice++) {
						temp = procurarPonto(numeroPontoEmbarque);
						tempRotaEmbarqueDestino.getJSONObject(indice).put("dados_ponto_embarque", temp.getJSONObject(0));

						temp = procurarPonto(numeroPontoDestino);
						tempRotaEmbarqueDestino.getJSONObject(indice).put("dados_ponto_destino", temp.getJSONObject(0));

						codigoLinha = tempRotaEmbarqueDestino.getJSONObject(0).getString("codigo_linha");
						temp = procurarLinha(codigoLinha);
						tempRotaEmbarqueDestino.getJSONObject(indice).put("dados_segunda_linha", temp.getJSONObject(0));

						temp = consultarInfoRotaSimples(numeroPontoEmbarque, numeroPontoDestino, codigoLinha);
						tempRotaEmbarqueDestino.getJSONObject(indice).put("info_rota_segunda", temp);
					}
				}

				rotasOrigemDesembarque.put(tempRotaOrigemDesembarque);
				rotasEmbarqueDestino.put(tempRotaEmbarqueDestino);
			}
		}

		if(rotasOrigemDesembarque.length() > 0) {
			JSONObject primeiroElemento = rotasOrigemDesembarque.getJSONArray(0).getJSONObject(0);
			dadosRotas.put("origem_terminal", new JSONArray().put(primeiroElemento));
		} else {
			dadosRotas.put("origem_terminal", rotasOrigemDesembarque);
		}
		
		if(rotasEmbarqueDestino.length() > 0) {
			JSONObject primeiroElemento = rotasEmbarqueDestino.getJSONArray(0).getJSONObject(0);
			dadosRotas.put("terminal_destino", new JSONArray().put(primeiroElemento));
		} else {
			dadosRotas.put("terminal_destino", rotasEmbarqueDestino);
		}
		
		return rotaConectada.put(dadosRotas);
	}

	private HashMap<String, JSONObject> JSONArrayTerminaisToHashMap(JSONArray jsonArrayTerminais) {
		if (jsonArrayTerminais == null || jsonArrayTerminais.length() == 0) {
			return null;
		}

		String key;
		JSONObject value;
		HashMap<String, JSONObject> terminais = new HashMap<>();
		for (int indice = 0; indice < jsonArrayTerminais.length(); indice++) {
			value = jsonArrayTerminais.getJSONObject(indice);
			key = formatarNomeTerminal(value.getString("endereco"));

			if (key == null) {
				continue;
			}

			terminais.put(key, value);
		}

		return terminais;
	}

	private String formatarNomeTerminal(String nomeTerminalNaoFormatado) {
		if (Validacao.ehStringVazia(nomeTerminalNaoFormatado)) {
			return null;
		}

		String nomeFormatado = null;
		String[] nomesSeparados = nomeTerminalNaoFormatado.split(" - ");
		if (nomesSeparados.length > 0) {
			nomeFormatado = nomesSeparados[0];
			nomeFormatado.replaceAll("  ", " ");
		}

		return nomeFormatado;
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
			 .where("R.ponto_saida = P.numero_ponto");

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
			 .where("R.ponto_chegada = P.numero_ponto");

		return executar.queryExecutor(query);
	}

	// ========================= FUNÇÕES AUXILIARES =========================
	// TODO revisão querySecundaria
	public JSONArray procurarPonto(int numeroPonto) {
		QueryMaker queryPrincipal = new QueryMaker();
		queryPrincipal.select("numero_ponto", "endereco", "tipo, ST_AsGeoJSON(geom, 15, 0) as geojson")
					  .from("pontos_de_onibus")
					  .where("numero_ponto ", numeroPonto);

		QueryMaker querySecundaria = new QueryMaker();
		querySecundaria.select("A.tipo", "COUNT(A.tipo) as count_tipos", "B.peso")
					   .from("crowdsourcing_pontos A, crowdsourcing_regras B")
					   .where("A.tipo = B.id")
					   .where("A.numero_ponto", numeroPonto)
					   .groupBy("A.tipo", "B.peso");

		JSONArray principal = executar.queryExecutor(queryPrincipal);
		JSONArray secundaria = executar.queryExecutor(querySecundaria);

		// Acrescenta o resultado da segunda query ao JSON resultante da primeira query.
		principal.getJSONObject(0).put("ocorrencias", secundaria);

		return principal;
	}

	// TODO revisão querySecundaria
	public JSONArray procurarLinha(String codigoLinha) {
		QueryMaker queryPrincipal = new QueryMaker();
		queryPrincipal.select("codigo_linha", "nome_linha", "cor", "categoria", "apenas_cartao")
					  .from("linhas_de_onibus")
					  .where("codigo_linha", codigoLinha);

		QueryMaker querySecundaria = new QueryMaker();
		querySecundaria.select("B.id", "COUNT(B.id) as count_tipos", "B.peso")
					   .from("crowdsourcing_linhas A, crowdsourcing_regras B")
					   .where("A.tipo = B.id")
					   .where("A.codigo_linha", codigoLinha)
					   .groupBy("B.id", "B.peso");

		JSONArray principal = executar.queryExecutor(queryPrincipal);
		JSONArray secundaria = executar.queryExecutor(querySecundaria);

		// Acrescenta o resultado da segunda query ao JSON resultante da primeira query.
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

		String subQuery = "(A.seq >= (SELECT MIN(seq) FROM pontos_de_onibus WHERE codigo_linha = A.codigo_linha AND numero_ponto = ':X' AND direcao = A.direcao) "
				+ "AND A.seq <= (SELECT MAX(seq) FROM pontos_de_onibus WHERE codigo_linha = A.codigo_linha AND numero_ponto = ':Y' AND direcao = A.direcao))";

		QueryMaker query = new QueryMaker();
		query.select("DISTINCT A.seq", "A.numero_ponto", "A.codigo_linha", "ST_AsGeoJSON(geom, 15, 0) geojson")
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
			 .where("A.direcao = B.direcao")
			 .where("(position('SITES' in A.endereco) = 1 OR position('Terminal' in A.endereco) = 1")
			 .where("(A.tipo = 'Plataforma' OR A.tipo = 'Estação tubo') OR position('Estação Tubo' in A.endereco) = 1)");

		return executar.queryExecutor(query);
	}
}