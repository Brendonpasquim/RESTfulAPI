package parameters;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.ws.rs.ext.ParamConverter;

import br.com.starmetal.parses.Parser;

public class LocalDateConverter implements ParamConverter<LocalDate>{

	@Override
	public LocalDate fromString(String value) {		
		return Parser.parseStringToLocalDate(value, DateTimeFormatter.ISO_LOCAL_DATE);
	}

	@Override
	public String toString(LocalDate value) {
		return value.toString();
	}

}
