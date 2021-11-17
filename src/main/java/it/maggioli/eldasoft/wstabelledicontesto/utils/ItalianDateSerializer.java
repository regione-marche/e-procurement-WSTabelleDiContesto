package it.maggioli.eldasoft.wstabelledicontesto.utils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

/**
 * Classe di utilit&agrave; per la serializzazione in formato JSON dei campi
 * data senza utilizzare il timezone del server ma mantenendo la data pura a
 * partire dall'oggetto java.util.Date.
 * 
 * @author stefano.sabbadin
 */
public class ItalianDateSerializer extends StdSerializer<Date> {

	/**
	 * UID.
	 */
	private static final long serialVersionUID = 4872322425127260253L;
	
	private SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

	public ItalianDateSerializer() {
		this(null);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ItalianDateSerializer(Class t) {
		super(t);
	}

	@Override
	public void serialize(Date value, JsonGenerator gen, SerializerProvider arg2)
			throws IOException, JsonProcessingException {
		gen.writeString(formatter.format(value));
	}
}

