package parameters;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.time.LocalDate;

import javax.ws.rs.ext.ParamConverter;
import javax.ws.rs.ext.ParamConverterProvider;
import javax.ws.rs.ext.Provider;

@Provider
public class LocalDateConverterProvider implements ParamConverterProvider{

	@SuppressWarnings("unchecked")//A classe LocalDateConverter implementa a interface ParamConverter, garantindo a segurança do cast.
	@Override
	public <T> ParamConverter<T> getConverter(Class<T> rawType, Type genericType, Annotation[] annotations) {
		if(rawType.equals(LocalDate.class)) {
			return (ParamConverter<T>) new LocalDateConverter();
		}
		return null;
	}

}
