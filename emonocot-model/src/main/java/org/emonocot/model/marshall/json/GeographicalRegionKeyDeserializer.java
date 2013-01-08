package org.emonocot.model.marshall.json;

import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.deser.StdKeyDeserializer;
import org.emonocot.model.constants.Location;
import org.emonocot.model.convert.StringToLocationConverter;

/**
 *
 * @author ben
 *
 */
public class GeographicalRegionKeyDeserializer extends StdKeyDeserializer {
    /**
     *
     */
    private StringToLocationConverter converter = new StringToLocationConverter();

    /**
     *
     */
    public GeographicalRegionKeyDeserializer() {
        super(Location.class);
    }

    /**
     * @param key the map key as a string
     * @param deserializationContext the deserialization context
     * @return the deserialized object
     * @throws Exception if there is an issue
     */
    @Override
    protected final Object _parse(final String key,
            final DeserializationContext deserializationContext)
            throws Exception {
        return converter.convert(key);

    }
}
