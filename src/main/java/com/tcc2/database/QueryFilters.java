package com.tcc2.database;

import br.com.starmetal.database.postgresql.QueryMaker;
import br.com.starmetal.validacoes.Validacao;

public class QueryFilters {

	/**
	 * Adiciona o filtro por Bairro utilizando uma tabela temporária criada com a KEYWORD WITH, do POSTGRESQL.
	 * 
	 * @param query
	 * @param bairro
	 * @return
	 */
    public static QueryMaker adicionarFiltroBairroWith(QueryMaker query, String from, String bairro) {
    	if(Validacao.saoParametrosInvalidos(query, from, bairro)) {
    		return query;
    	}
    	
    	String where = "ST_Within(P.geom, ST_Transform(ST_setSRID((SELECT geom FROM divisa_de_bairros WHERE nome ilike '%:bairro%' limit 1), 29192), 4326))";
    	
    	QueryMaker tabelaAuxBairro = new QueryMaker();
    	tabelaAuxBairro.select("DISTINCT *")
    				   .from("pontos_de_onibus P")
    				   .where(where).setParameter("bairro", bairro);
    	
    	query.with(tabelaAuxBairro, "filtro_bairro")
    		 .from(from + ", filtro_bairro bairro")
    		 .where("aux.numero_ponto = bairro.numero_ponto");
  	
    	return query;
    }
	
    /**
     * Adiciona o filtro por Bairro utilizando um INNER JOIN. O parâmetro joinColumnName define a condição
     * de união entre a tabela da query principal e a tabela do filtro por bairro.
     * 
     * @param query
     * @param bairro
     * @param joinColumnName
     * @return
     */
    public static QueryMaker adicionarFiltroBairro(QueryMaker query, String bairro, String joinColumnName) {
    	if(Validacao.saoParametrosInvalidos(query, bairro, joinColumnName)) {
    		return query;
    	}
    	
    	String where = "ST_Within(P.geom, ST_Transform(ST_setSRID((SELECT geom FROM divisa_de_bairros WHERE nome ilike '%:bairro%'), 29192), 4326))";
    	
    	QueryMaker tabelaAuxBairro = new QueryMaker();
    	tabelaAuxBairro.select("DISTINCT *")
    				   .from("pontos_de_onibus P")
    				   .where(where).setParameter("bairro", bairro);
    	
    	String table = "(" + tabelaAuxBairro.getQuery() + ") pontos";
    	String condition = joinColumnName + " = " + "pontos.numero_ponto";
    	query.innerJoin(table, condition);
  	
    	return query;
	}

}
