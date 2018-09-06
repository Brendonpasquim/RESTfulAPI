package com.tcc2.beans;

public class PontosProximos {

	private int numeroPonto;
	private String endereco;
	private String tipo;
	private int codigoLinha;
	private double latitude;
	private double longitude;
	private String nomeLinha;
	private String cor;
	private String apenasCartao;
	private String geojson;

	public PontosProximos() {
	}

	public int getNumeroPonto() {
		return numeroPonto;
	}

	public void setNumeroPonto(int numeroPonto) {
		this.numeroPonto = numeroPonto;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public int getCodigoLinha() {
		return codigoLinha;
	}

	public void setCodigoLinha(int codigoLinha) {
		this.codigoLinha = codigoLinha;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public String getNomeLinha() {
		return nomeLinha;
	}

	public void setNomeLinha(String nomeLinha) {
		this.nomeLinha = nomeLinha;
	}

	public String getCor() {
		return cor;
	}

	public void setCor(String cor) {
		this.cor = cor;
	}

	public String getApenasCartao() {
		return apenasCartao;
	}

	public void setApenasCartao(String apenasCartao) {
		this.apenasCartao = apenasCartao;
	}

	public String getGeojson() {
		return geojson;
	}

	public void setGeojson(String geojson) {
		this.geojson = geojson;
	}

}
