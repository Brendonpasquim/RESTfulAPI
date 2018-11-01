package com.tcc2.testes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class TestesConcorrencia {

	public static void main(String[] args) {
		for (int i = 0; i < 100; i++) {
			new Thread(new ThreadConsulta(i + 1)).start();
		}
	}
}

class ThreadConsulta implements Runnable {
	private static int HTTP_COD_SUCESSO = 200;
	private int THREAD_ID = 0;

	public ThreadConsulta(int THREAD_ID) {
		this.THREAD_ID = THREAD_ID;
	}
	
	@Override
	public void run() {
		try {			
			System.out.println(String.format("Requisição [%d] INICIADA", ++THREAD_ID));
			URL url = new URL("http://localhost:8080/restfulapi/rotas/rota_simples_dois_pontos?numero_ponto_origem=150009&numero_ponto_destino=110026");
			HttpURLConnection con = (HttpURLConnection) url.openConnection();

			if (con.getResponseCode() != HTTP_COD_SUCESSO) {
				throw new RuntimeException("HTTP error code : " + con.getResponseCode());
			}

			new BufferedReader(new InputStreamReader((con.getInputStream())));
			con.disconnect();

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(String.format("Requisição [%d] FINALIZADA", THREAD_ID));
	}

}