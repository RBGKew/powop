package org.emonocot.model.marshall.json;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.emonocot.model.Base;

/**
 *
 * @author ben
 *
 * @param <T>
 */
public class BaseSerializer<T extends Base> extends JsonSerializer<T> {

    @Override
    public final void serialize(final T t, final JsonGenerator jsonGenerator,
            final SerializerProvider serializerProvider) throws IOException {
    	try {
            jsonGenerator.writeString(t.getIdentifier());
    	} catch(Exception e) {
    		jsonGenerator.writeNull();
    	}
    }
}
