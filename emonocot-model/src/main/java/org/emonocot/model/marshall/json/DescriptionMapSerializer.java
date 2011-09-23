package org.emonocot.model.marshall.json;

import java.io.IOException;
import java.util.Map;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.emonocot.model.description.Content;
import org.emonocot.model.description.Feature;

/**
 *
 * @author ben
 *
 */
public class DescriptionMapSerializer extends
        JsonSerializer<Map<Feature, Content>> {

    @Override
    public final void serialize(Map<Feature, Content> map,
            JsonGenerator jsonGenerator,
            SerializerProvider serializationProvider) throws IOException,
            JsonProcessingException {
        jsonGenerator.writeStartArray();
        for (Content content : map.values()) {
            jsonGenerator.writeObject(content);
        }
        jsonGenerator.writeEndArray();

    }

}
