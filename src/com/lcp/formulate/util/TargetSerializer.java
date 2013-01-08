package com.lcp.formulate.util;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

import com.lcp.formulate.entities.ormlite.ViewField;

public class TargetSerializer extends JsonSerializer<ViewField> {

	@Override
	public void serialize(ViewField vf, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
		try {
		jgen.writeStartObject();
		jgen.writeFieldName("id");
		jgen.writeNumber(vf.getId());
		jgen.writeFieldName("fieldType");
		jgen.writeStartObject();
		jgen.writeFieldName("id");
		jgen.writeNumber(vf.getFieldType().getId());
		jgen.writeEndObject();
		jgen.writeEndObject();
		} catch (Exception x) {
			x.printStackTrace();
		}
	}

}
