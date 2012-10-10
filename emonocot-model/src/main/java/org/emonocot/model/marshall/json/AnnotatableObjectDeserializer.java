package org.emonocot.model.marshall.json;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.emonocot.api.Service;
import org.emonocot.model.Base;

/**
 *
 * @author ben
 *
 */
public class AnnotatableObjectDeserializer extends JsonDeserializer<Base> {

    /**
     *
     */
    private Set<Service<? extends Base>> services = new HashSet<Service<? extends Base>>();

    /**
     *
     * @param service Set the service
     */
    public final void addService(final Service<? extends Base> service) {
        if (service != null) {
            this.services.add(service);
        }
    }

    @Override
    public final Base deserialize(final JsonParser jsonParser,
            final DeserializationContext deserializationContext)
            throws IOException {
        String identifier = jsonParser.getText();
        if (identifier == null) {
            return null;
        }
        for (Service<? extends Base> service : services) {
            Base base = service.find(identifier);
            if (base != null) {
                return base;
            }
        }
        return null;
    }
}
