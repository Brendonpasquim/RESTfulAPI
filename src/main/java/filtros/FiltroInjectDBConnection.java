package filtros;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

import com.tcc2.database.DAOBaseUTFPR;

import br.com.starmetal.exceptions.DatabaseException;

@WebFilter("/*")
public class FiltroInjectDBConnection implements Filter{
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		ServletContext context = request.getServletContext();
		
		//Recupera o recurso de contexto
		DAOBaseUTFPR daoUTFPR = (DAOBaseUTFPR) context.getAttribute("daoUTFPR");
		Logger.getLogger(FiltroInjectDBConnection.class.getName()).log(Level.INFO, "Estabelecendo conexão remota com Base de Dados da UTFPR.");
		Connection conexaoBD = daoUTFPR.getConnectionFactory().getConnectionWithSSH();
		
		//Seta a conexão no contexto
		Logger.getLogger(FiltroInjectDBConnection.class.getName()).log(Level.INFO, "Disponibilizando conexão com Base de Dados no Contexto de Aplicação.");
		context.setAttribute("connection", conexaoBD);
		
		//Encaminha a execução para o resource de destino.
		chain.doFilter(request, response);
		
		try {
			Logger.getLogger(FiltroInjectDBConnection.class.getName()).log(Level.INFO, "Encerrando conexão remota com Base de Dados da UTFPR.");
			conexaoBD.close();
		} catch (SQLException sqle) {
			throw new DatabaseException("Falha ao encerrar conexão de banco de dados de contexto", sqle.getMessage());
		}
	}

}
