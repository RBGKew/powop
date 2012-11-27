package org.emonocot.model.marshall.json;

import java.io.IOException;
import java.util.Map;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.emonocot.model.Description;
import org.emonocot.model.constants.DescriptionType;

/**
 *
 * @author ben
 *
 */
public class DescriptionMapSerializer extends
        JsonSerializer<Map<DescriptionType, Description>> {

    @Override
    public final void serialize(final Map<DescriptionType, Description> map,
            final JsonGenerator jsonGenerator,
            final SerializerProvider serializationProvider) throws IOException {
        jsonGenerator.writeStartArray();
        for (Description content : map.values()) {
            jsonGenerator.writeObject(content);
        }
        jsonGenerator.writeEndArray();

    }

}
