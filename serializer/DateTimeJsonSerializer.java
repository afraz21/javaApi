package org.iqvis.nvolv3.serializer;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import com.fasterxml.jackson.core.JsonProcessingException;

public class DateTimeJsonSerializer extends JsonSerializer<DateTime> {

	public static final String dateFormat = ("yyyy-MM-dd'T'HH:mm:ss.SSS");

	@Override
	public void serialize(DateTime date, JsonGenerator gen, SerializerProvider provider) throws IOException, JsonProcessingException {

		String formattedDate = DateTimeFormat.forPattern(dateFormat).print(date);

		gen.writeString(formattedDate);
	}
}
