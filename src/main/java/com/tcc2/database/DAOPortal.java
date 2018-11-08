package com.tcc2.database;

import java.time.LocalDate;
import java.time.LocalTime;

import org.json.JSONArray;

import br.com.starmetal.database.postgresql.QueryMaker;
import br.com.starmetal.util.FiltersUtil;
import br.com.starmetal.validacoes.Validacao;

/**
 *
 * @author brendon
 */
public class DAOPortal {
    
    private final Executor executar;

    public DAOPortal(DAOManager daoManager) {
        this.executar = daoManager.getExecutor();
    }
    
    private QueryMaker adicionarFiltroBairro(QueryMaker query, String bairro) {
    	if(Validacao.saoParametrosInvalidos(query, bairro)) {
    		return query;
    	}
    	
    	String where = "ST_Within(P.geom, ST_Transform(ST_setSRID((SELECT geom FROM divisa_de_bairros WHERE nome ilike ':bairro'), 29192), 4326))";
    	
    	QueryMaker tabelaAuxBairro = new QueryMaker();
    	tabelaAuxBairro.select("DISTINCT *")
    				   .from("pontos_de_onibus P")
    				   .where(where).setParameter("bairro", bairro);
    	
    	query.with(tabelaAuxBairro, "tabela_pontos")
    		 .from("tabela_aux aux, tabela_pontos pontos")
    		 .where("aux.numero_ponto = pontos.numero_ponto");
  	
    	return query;
    }
    
    public JSONArray consultarMapaOrigem(LocalDate diaInicio, LocalDate diaFim, LocalTime horaInicio, LocalTime horaFim, String bairro){
        QueryMaker queryWith = new QueryMaker();
        queryWith.select("R.data_viagem", "R.horario_saida", "R.ponto_saida", "R.codigo_linha", "P.numero_ponto", "P.endereco", "P.tipo", "ST_AsGeoJSON(P.geom, 15, 0) AS geojson")
                 .from("relatorio_viagem R, pontos_de_onibus P")
                 .where("R.ponto_saida = P.numero_ponto");
        
        QueryMaker query = new QueryMaker();
        query.with(queryWith, "tabela_aux")
             .select("ponto_saida", "COUNT(ponto_saida)", "aux.endereco", "aux.tipo", "aux.codigo_linha", "geojson")
             .from("tabela_aux aux")
             .where(FiltersUtil.dateBetween("aux.data_viagem", diaInicio, diaFim))
             .where(FiltersUtil.timeBetween("aux.horario_saida", horaInicio, horaFim))             
             .groupBy("ponto_saida", "aux.endereco", "aux.tipo", "aux.codigo_linha", "geojson")
             .orderBy("ponto_saida");
        
        adicionarFiltroBairro(query, bairro);
        
        return executar.queryExecutor(query);
    }
    
    public JSONArray consultarMapaDestino(LocalDate diaInicio, LocalDate diaFim, LocalTime horaInicio, LocalTime horaFim, String bairro){
        QueryMaker queryWith = new QueryMaker();
        queryWith.select("R.data_viagem", "R.horario_chegada", "R.ponto_chegada", "P.numero_ponto", "P.endereco", "P.tipo", "R.codigo_linha", "ST_AsGeoJSON(P.geom, 15, 0) AS geojson")
                 .from("relatorio_viagem R, pontos_de_onibus P")
                 .where("R.ponto_chegada = P.numero_ponto");
                 
        QueryMaker query = new QueryMaker();
        query.with(queryWith, "tabela_aux")
             .select("aux.ponto_chegada", "COUNT(aux.ponto_chegada)", "aux.endereco", "aux.tipo", "aux.codigo_linha", "aux.geojson")
             .from("tabela_aux aux")
             .where(FiltersUtil.dateBetween("aux.data_viagem", diaInicio, diaFim))
             .where(FiltersUtil.timeBetween("aux.horario_saida", horaInicio, horaFim))
             .groupBy("aux.ponto_chegada", "aux.endereco", "aux.tipo", "aux.codigo_linha", "aux.geojson")
             .orderBy("aux.ponto_chegada");
        
        adicionarFiltroBairro(query, bairro);
        
        return executar.queryExecutor(query);
    }
    
    public JSONArray consultarRelatorioLinhas(){
        QueryMaker query = new QueryMaker();
        query.select("R.codigo_linha", "L.nome_linha", "L.categoria", "COUNT(R.codigo_linha) AS quantidade")
             .from("relatorio_viagem R, linhas_de_onibus L")
             .where("R.codigo_linha = L.codigo_linha")
             .groupBy("R.codigo_linha", "L.nome_linha", "L.categoria")
             .orderBy("quantidade");
        
        return executar.queryExecutor(query);
    }
    
    public JSONArray consultarRelatorioPontosOrigem(){
        QueryMaker queryWith = new QueryMaker();
        queryWith.select("R.data_viagem", "R.horario_chegada", "R.ponto_chegada", "P.endereco", "P.tipo", "R.codigo_linha")
                 .from("relatorio_viagem R, pontos_de_onibus P")
                 .where("R.ponto_chegada = P.numero_ponto")
                 .where("P.codigo_linha = R.codigo_linha");
        
        QueryMaker query = new QueryMaker();
        query.with(queryWith, "tabela_aux")
             .select("ponto_chegada", "COUNT(ponto_chegada) as quantidade", "endereco", "tipo", "codigo_linha")
             .from("tabela_aux")
             .groupBy("ponto_chegada", "endereco", "tipo", "codigo_linha")
             .orderBy("ponto_chegada");
        
        return executar.queryExecutor(query);
    }
    
    public JSONArray consultarRelatorioPontosDestino(){
        QueryMaker queryWith = new QueryMaker();
        queryWith.select("R.data_viagem", "R.horario_saida", "R.ponto_saida", "P.endereco", "P.tipo", "R.codigo_linha")
                 .from("relatorio_viagem R, pontos_de_onibus P")
                 .where("R.ponto_saida = P.numero_ponto")
                 .where("P.codigo_linha = R.codigo_linha");
        
        QueryMaker query = new QueryMaker();
        query.with(queryWith, "tabela_aux")
             .select("ponto_saida", "COUNT(ponto_saida) as quantidade", "endereco", "tipo", "codigo_linha")
             .from("tabela_aux")
             .groupBy("ponto_saida", "endereco", "tipo", "codigo_linha")
             .orderBy("ponto_saida");
        
        return executar.queryExecutor(query);        
    }
    
    public JSONArray consultarCrowdsourcingPontosMapa(LocalDate diaInicio, LocalDate diaFim, LocalTime horaInicio, LocalTime horaFim, String bairro){
        QueryMaker queryWith = new QueryMaker();
        queryWith.select("DISTINCT C.numero_ponto", "P.endereco", "P.tipo", "R.nome", "C.dia", "C.horario", "ST_AsGeoJSON(P.geom, 15, 0) AS geojson")
                 .from("crowdsourcing_pontos C, pontos_de_onibus P, crowdsourcing_regras R")
                 .where("P.tipo = R.id")
                 .where("P.numero_ponto = C.numero_ponto");
                 
        QueryMaker query = new QueryMaker();
        query.with(queryWith, "tabela_aux")
             .select("aux.numero_ponto", "aux.endereco", "aux.tipo", "aux.nome", "COUNT(aux.nome) as quantidade", "aux.geojson")
             .from("tabela_aux aux")
             .where(FiltersUtil.dateBetween("aux.data_viagem", diaInicio, diaFim))
             .where(FiltersUtil.timeBetween("aux.horario_saida", horaInicio, horaFim))
             .groupBy("aux.numero_ponto", "aux.endereco", "aux.tipo", "aux.nome", "aux.geojson")
             .orderBy("aux.numero_ponto", "aux.nome");
        
        adicionarFiltroBairro(query, bairro);
        
        return executar.queryExecutor(query);
    }
    
    public JSONArray consultarCrowdsourcingRegrasRelatorio(){
        QueryMaker queryWith = new QueryMaker();
        queryWith.select("DISTINCT C.numero_ponto", "P.endereco", "P.tipo", "R.nome", "C.dia", "C.horario")
                 .from("crowdsourcing_pontos C, pontos_de_onibus P, crowdsourcing_regras R")
                 .where("C.tipo = R.id")
                 .where("P.numero_ponto = C.numero_ponto");
        
        QueryMaker query = new QueryMaker();
        query.with(queryWith, "tabela_aux")
             .select("numero_ponto", "endereco", "tipo", "nome", "COUNT(nome) as quantidade")
             .from("tabela_aux")
             .groupBy("numero_ponto", "endereco", "tipo", "nome")
             .orderBy("numero_ponto", "nome");
        
        return executar.queryExecutor(query);
    }
    
    public JSONArray consultarCrowdsourcingLinhasMapa(){
        QueryMaker queryWith = new QueryMaker();
        queryWith.select("C.codigo_linha", "L.nome_linha", "L.categoria", "R.nome", "C.dia", "C.horario")
                 .from("crowdsourcing_linhas C, linhas_de_onibus L, crowdsourcing_regras R")
                 .where("C.tipo = R.id")
                 .where("L.codigo_linha = C.codigo_linha");
        
        QueryMaker query = new QueryMaker();
        query.with(queryWith, "tabela_aux")
             .select("codigo_linha", "nome_linha", "categoria", "nome", "COUNT(nome) as quantidade")
             .from("tabela_aux")
             .groupBy("codigo_linha", "nome_linha", "categoria",  "nome")
             .orderBy("codigo_linha", "nome");
    
        return executar.queryExecutor(query);
    }
}
