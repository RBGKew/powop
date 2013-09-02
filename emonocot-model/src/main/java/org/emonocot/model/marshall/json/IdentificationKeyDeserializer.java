package org.emonocot.model.marshall.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import org.emonocot.model.IdentificationKey;

/**
 *
 * @author ben
 *
 */
public class IdentificationKeyDeserializer extends BaseDeserializer<IdentificationKey> {

    /**
     *
     */
    public IdentificationKeyDeserializer() {
        super(IdentificationKey.class);
    }

    @Override
    public final IdentificationKey deserialize(final JsonParser jsonParser,
            final DeserializationContext deserializationContext)
            throws IOException {
        String identifier = jsonParser.getText();
        if (service != null) {
            return service.load(identifier);
        } else {
        	return null;
        }
    }

}
