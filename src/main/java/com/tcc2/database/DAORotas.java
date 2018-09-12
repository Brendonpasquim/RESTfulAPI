package com.tcc2.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONArray;

import br.com.starmetal.database.postgresql.QueryMaker;
import br.com.starmetal.exceptions.DatabaseException;

public class DAORotas {

	private Connection connection;
	
	public DAORotas(Connection connection) {
		this.connection = connection;
	}
	
	/**
	 * Executa a query
	 * @param query
	 * @param connection
	 * @return
	 */
	private JSONArray QueryExecutor(QueryMaker query) {
		if(query == null) {
			return new JSONArray();
		}
		
		PreparedStatement statement = null;
		ResultSet result = null;
		JSONArray jsonArray;
		try {
			statement = connection.prepareStatement(query.getQuery());
            result = statement.executeQuery();
			jsonArray = Parser.toJSON(result);
			
		} catch(SQLException sqle) {
			throw new DatabaseException("Falha ao executar consulta na base de Dados da UTFPR.", sqle.getMessage());
		} finally {
			try{
				
				if(statement 	!= null) statement.close();
				if(result 		!= null) result.close();
				
			} catch(SQLException sqle) {
				throw new DatabaseException("Falha ao encerrar recursos de conexão com base de dados.", sqle.getMessage());
			}
		}
		
		return jsonArray;
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
		
		return QueryExecutor(query);
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
		
		return QueryExecutor(query);
	}
	
	//========================= FUNÇÕES AUXILIARES =========================
	
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
		
		return QueryExecutor(query);
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
		
		return QueryExecutor(query);
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
		
		return QueryExecutor(query);
	}
}