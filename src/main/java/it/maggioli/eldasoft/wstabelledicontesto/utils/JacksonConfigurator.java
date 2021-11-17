package it.maggioli.eldasoft.wstabelledicontesto.utils;

import java.util.Date;

import javax.ws.rs.Produces;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import org.glassfish.jersey.jackson.JacksonFeature;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;

@Provider
@Produces("application/json")
public class JacksonConfigurator extends JacksonFeature implements ContextResolver<ObjectMapper>
		 {

	public JacksonConfigurator() {
		SimpleModule module = new SimpleModule();
		module.addSerializer(Date.class, new ItalianDateSerializer());
		mapper.registerModule(new JaxbAnnotationModule());
		mapper.registerModule(module);
	}

	@Override
	public ObjectMapper getContext(Class<?> clazz) {
		return mapper;
	}

	private final ObjectMapper mapper = new ObjectMapper();
	
}
