package com.tcc2.parameters;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import javax.ws.rs.ext.ParamConverter;

import br.com.starmetal.parses.Parser;

public class LocalTimeConverter implements ParamConverter<LocalTime>{

	@Override
	public LocalTime fromString(String value) {
		return Parser.parseStringToLocalTime(value, DateTimeFormatter.ISO_LOCAL_TIME);
	}

	@Override
	public String toString(LocalTime value) {
		return value.toString();
	}

}
