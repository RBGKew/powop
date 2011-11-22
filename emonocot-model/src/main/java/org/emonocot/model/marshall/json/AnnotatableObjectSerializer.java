package org.emonocot.model.marshall.json;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.emonocot.model.common.Base;

/**
 *
 * @author ben
 *
 */
public class AnnotatableObjectSerializer extends JsonSerializer<Base> {

    @Override
    public final void serialize(final Base b, final JsonGenerator jsonGenerator,
            final SerializerProvider serializerProvider) throws IOException {
        if (b != null) {
            jsonGenerator.writeString(b.getIdentifier());
        }
    }
}
