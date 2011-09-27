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
 * @param <T>
 */
public abstract class BaseSerializer<T extends Base> extends JsonSerializer<T> {

    @Override
    public final void serialize(final T t, final JsonGenerator jsonGenerator,
            final SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(t.getIdentifier());
    }
}