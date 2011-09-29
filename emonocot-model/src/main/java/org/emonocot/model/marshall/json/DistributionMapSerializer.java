package org.emonocot.model.marshall.json;

import java.io.IOException;
import java.util.Map;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.emonocot.model.description.Distribution;
import org.emonocot.model.geography.GeographicalRegion;

/**
 *
 * @author ben
 *
 */
public class DistributionMapSerializer extends
        JsonSerializer<Map<GeographicalRegion, Distribution>> {

    @Override
    public final void serialize(final Map<GeographicalRegion, Distribution> map,
            final JsonGenerator jsonGenerator,
            final SerializerProvider serializationProvider) throws IOException {
        jsonGenerator.writeStartArray();
        for (Distribution distribution : map.values()) {
            jsonGenerator.writeObject(distribution);
        }
        jsonGenerator.writeEndArray();

    }

}
