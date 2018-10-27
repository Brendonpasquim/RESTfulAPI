package com.tcc2.database;

import java.time.LocalDate;
import java.time.LocalTime;

import org.json.JSONArray;

import br.com.starmetal.database.postgresql.QueryMaker;
import br.com.starmetal.util.FiltersUtil;

/**
 *
 * @author brendon
 */
public class DAOPortal {
    
    private final Executor executar;

    public DAOPortal(DAOManager daoManager) {
        this.executar = daoManager.getExecutor();
    }
    
    public JSONArray consultarMapaOrigem(LocalDate diaInicio, LocalDate diaFim, LocalTime horaInicio, LocalTime horaFim, String tipoPonto, String tipoLinha, String bairro){
        // TODO Fazer validação de parâmetros
        
        QueryMaker queryWith = new QueryMaker();
        queryWith.select("R.data_viagem", "R.horario_saida", "R.ponto_saida", "P.endereco", "P.tipo", "R.codigo_linha", "ST_AsGeoJSON(P.geom, 15, 0) AS geojson")
                 .from("relatorio_viagem R, pontos_de_onibus P")
                 .where("R.ponto_saida = P.numero_ponto");
        
        QueryMaker query = new QueryMaker();
        query.with(queryWith, "tabela_aux")
             .select("ponto_saida", "COUNT(ponto_saida)", "endereco", "tipo", "codigo_linha", "geojson")
             .from("tabela_aux")
             .where(FiltersUtil.dateBetween("R.data_viagem", diaInicio, diaFim))
             .where(FiltersUtil.timeBetween("R.horario_saida", horaInicio, horaFim))
             .groupBy("ponto_saida", "endereco", "tipo", "codigo_linha", "geojson")
             .orderBy("ponto_saida");
        
        return executar.queryExecutor(query);
    }
    
    public JSONArray consultarMapaDestino(LocalDate diaInicio, LocalDate diaFim, LocalTime horaInicio, LocalTime horaFim){
        // TODO Fazer validação de parâmetros
        
        QueryMaker queryWith = new QueryMaker();
        queryWith.select("R.data_viagem", "R.horario_chegada", "R.ponto_chegada", "P.endereco", "P.tipo", "R.codigo_linha", "ST_AsGeoJSON(P.geom, 15, 0) AS geojson")
                 .from("relatorio_viagem R, pontos_de_onibus P")
                 .where("R.ponto_chegada = P.numero_ponto");
                 
        QueryMaker query = new QueryMaker();
        query.with(queryWith, "tabela_aux")
             .select("ponto_chegada", "COUNT(ponto_chegada)", "endereco", "tipo", "codigo_linha", "geojson")
             .from("tabela_aux")
             .where(FiltersUtil.dateBetween("R.data_viagem", diaInicio, diaFim))
             .where(FiltersUtil.timeBetween("R.horario_saida", horaInicio, horaFim))
             .groupBy("ponto_chegada", "endereco", "tipo", "codigo_linha", "geojson")
             .orderBy("ponto_chegada");
        
        return executar.queryExecutor(query);
    }
    
    public JSONArray consultarRelatorioLinhas(){
        // TODO Fazer validação de parâmetros
        QueryMaker query = new QueryMaker();
        query.select("R.codigo_linha", "L.nome_linha", "L.categoria", "COUNT(R.codigo_linha) AS quantidade")
             .from("relatorio_viagem R, linhas_de_onibus L")
             .where("R.codigo_linha = L.codigo_linha")
             .groupBy("R.codigo_linha", "L.nome_linha", "L.categoria")
             .orderBy("quantidade");
        
        return executar.queryExecutor(query);
    }
    
    public JSONArray consultarRelatorioPontosOrigem(){
        // TODO Fazer validação de parâmetros
        
        QueryMaker queryWith = new QueryMaker();
        queryWith.select("R.data_viagem", "R.horario_saida", "R.ponto_saida", "P.endereco", "P.tipo", "R.codigo_linha")
                 .from("relatorio_viagem R, pontos_de_onibus P")
                 .where("R.ponto_saida = P.numero_ponto");
        
        QueryMaker query = new QueryMaker();
        query.with(query, "tabela_aux")
             .select("ponto_saida", "COUNT(ponto_saida) as quantidade", "endereco", "tipo", "codigo_linha")
             .from("tabela_aux")
             .groupBy("ponto_saida", "endereco", "tipo", "codigo_linha")
             .orderBy("ponto_saida");
        
        return executar.queryExecutor(query);
    }
    
    public JSONArray consultarRelatorioPontosDestino(){
        // TODO Fazer validação de parâmetros
        
        QueryMaker queryWith = new QueryMaker();
        queryWith.select("R.data_viagem", "R.horario_saida", "R.ponto_chegada", "P.endereco", "P.tipo", "R.codigo_linha")
                 .from("relatorio_viagem R, pontos_de_onibus P")
                 .where("R.ponto_chegada = P.numero_ponto");
        
        QueryMaker query = new QueryMaker();
        query.with(query, "tabela_aux")
             .select("ponto_saida", "COUNT(ponto_chegada) as quantidade", "endereco", "tipo", "codigo_linha")
             .from("tabela_aux")
             .groupBy("ponto_chegada", "endereco", "tipo", "codigo_linha")
             .orderBy("ponto_chegada");
        
        return executar.queryExecutor(query);        
    }
    
    public JSONArray consultarCrowdsourcingPontosMapa(LocalDate diaInicio, LocalDate diaFim, LocalTime horaInicio, LocalTime horaFim, String tipoPonto, String tipoLinha, String bairro){
        // TODO Fazer validação de parâmetros
        
        QueryMaker queryWith = new QueryMaker();
        queryWith.select("DISTINCT C.numero_ponto", "P.endereco", "P.tipo", "R.nome", "C.dia", "C.horario", "ST_AsGeoJSON(P.geom, 15, 0) AS geojson")
                 .from("crowdsourcing_pontos C, pontos_de_onibus P, crowdsourcing_regras R")
                 .where("C.tipo = R.id")
                 .where("P.numero_ponto = C.numero_ponto");
                 
        QueryMaker query = new QueryMaker();
        query.with(queryWith, "tabela_aux")
             .select("numero_ponto", "endereco", "tipo", "nome", "COUNT(nome) as quantidade", "geojson")
             .from("tabela_aux")
             .where(FiltersUtil.dateBetween("R.data_viagem", diaInicio, diaFim))
             .where(FiltersUtil.timeBetween("R.horario_saida", horaInicio, horaFim))
             .groupBy("numero_ponto", "endereco", "tipo", "nome", "geojson")
             .orderBy("numero_ponto", "nome");
        
        return executar.queryExecutor(query);
    }
    
    public JSONArray consultarCrowdsourcingRegrasRelatorio(){
        // TODO Fazer validação de parâmetros
    
        QueryMaker queryWith = new QueryMaker();
        queryWith.select("DISTINCT C.numero_ponto", "P.endereco", "P.tipo", "R.nome", "C.dia", "C.horario")
                 .from("crowdsourcing_pontos C, pontos_de_onibus P, crowdsourcing_regras R")
                 .where("C.tipo = R.id")
                 .where("P.numero_ponto = C.numero_ponto");
        
        QueryMaker query = new QueryMaker();
        query.with(queryWith, "tabela_aux")
             .select("numero_ponto", "endereco", "tipo", "nome", "COUNT(nome) as quantidade")
             .from("tabela_aux")
             .groupBy("numero_ponto", "endereco", "tipo", "nome", "geojson")
             .orderBy("numero_ponto", "nome");
        
        return executar.queryExecutor(query);
    }
    
    public JSONArray consultarCrowdsourcingLinhasMapa(){
        // TODO Fazer validação de parâmetros
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
