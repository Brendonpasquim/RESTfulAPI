package com.tcc2.parameters;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.time.LocalTime;

import javax.ws.rs.ext.ParamConverter;
import javax.ws.rs.ext.ParamConverterProvider;
import javax.ws.rs.ext.Provider;

@Provider
public class LocalTimeConverterProvider implements ParamConverterProvider{

	@SuppressWarnings("unchecked")
	@Override
	public <T> ParamConverter<T> getConverter(Class<T> rawType, Type genericType, Annotation[] annotations) {
		if(rawType.equals(LocalTime.class)) {
			return (ParamConverter<T>) new LocalTimeConverter();
		}
		return null;
	}

}
