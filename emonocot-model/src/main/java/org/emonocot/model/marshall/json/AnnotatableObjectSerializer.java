package org.emonocot.model.marshall.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.emonocot.model.Base;

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
