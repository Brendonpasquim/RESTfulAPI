package com.tcc2.database;

import java.time.LocalDate;
import java.time.LocalTime;

import br.com.starmetal.database.postgresql.InsertMaker;
import br.com.starmetal.results.ResultType;
import br.com.starmetal.validacoes.Validacao;

public class DAOCrowdsourcing {

	private Executor executar = null;
	
	public DAOCrowdsourcing(DAOManager daoManager) {
		this.executar = daoManager.getExecutor();
	}
	
	public ResultType cadastrarRelatorioViagem(LocalDate data, LocalTime horarioSaida, int pontoSaida, LocalTime horarioChegada, int pontoChegada, String codigoLinha) {
		if(Validacao.saoParametrosInvalidos(data, horarioSaida, horarioChegada, codigoLinha)) {
			return ResultType.ERROR;
		}
		
		InsertMaker insert = new InsertMaker();
		insert.insertInto("relatorio_viagem")
			  .value("data_viagem", data)
			  .value("horario_saida", horarioSaida)
			  .value("ponto_saida", pontoSaida)
			  .value("horario_chegada", horarioChegada)
			  .value("ponto_chegada", pontoChegada)
			  .value("codigo_linha", codigoLinha);
		
		return executar.insertExecutor(insert);
	}
	
	public ResultType cadastrarCrowdsourcingPontos(int numeroPonto, LocalDate dia, LocalTime horario, int tipo) {
		if(Validacao.saoParametrosInvalidos(dia, horario)) {
			return ResultType.ERROR;
		}
		
		InsertMaker insert = new InsertMaker();
		insert.insertInto("crowdsourcing_pontos")
			  .value("numero_ponto", numeroPonto)
			  .value("dia", dia)
			  .value("horario", horario)
			  .value("tipo", tipo);
		
		return executar.insertExecutor(insert);
	}
	
	public ResultType cadastrarCrowdsourcingLinhas(String codigoLinha, LocalDate dia, LocalTime horario, double latitude, double longitude, int tipo) {
		if(Validacao.saoParametrosInvalidos(codigoLinha, dia, horario)) {
			return ResultType.ERROR;
		}
		
		InsertMaker insert = new InsertMaker();
		insert.insertInto("crowdsourcing_linhas")
			  .value("codigo_linha", codigoLinha)
			  .value("dia", dia)
			  .value("horario", horario)
			  .value("lat", latitude)
			  .value("lon", longitude)
			  .value("tipo", tipo);
		
		return executar.insertExecutor(insert);
	}
}
