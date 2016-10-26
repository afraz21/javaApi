package org.iqvis.nvolv3.serializer;

import java.io.IOException;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonProcessingException;

public class TimeJsonDeSerializer extends JsonDeserializer<DateTime> {

	public static final String dateFormat = ("h:mm aa");

	private final DateTimeFormatter formatter = DateTimeFormat.forPattern(dateFormat);

	@Override
	public DateTime deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		return formatter.parseDateTime(jp.getText());
	}

}