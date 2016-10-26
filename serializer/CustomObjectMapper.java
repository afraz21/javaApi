package org.iqvis.nvolv3.serializer;

import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ser.CustomSerializerFactory;
import org.joda.time.DateTime;

public class CustomObjectMapper extends ObjectMapper {

	public CustomObjectMapper() {
		CustomSerializerFactory factory = new CustomSerializerFactory();
		factory.addSpecificMapping(DateTime.class, new JodaDateTimeJsonSerializer());
		this.setSerializerFactory(factory);
		this.setVisibility(JsonMethod.FIELD, org.codehaus.jackson.annotate.JsonAutoDetect.Visibility.ANY);

	}

}