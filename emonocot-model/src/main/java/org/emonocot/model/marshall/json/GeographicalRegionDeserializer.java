package org.emonocot.model.marshall.json;

import java.io.IOException;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.deser.StdDeserializer;
import org.emonocot.model.convert.StringToLocationConverter;
import org.emonocot.model.geography.Location;

/**
 *
 * @author ben
 *
 */
public class GeographicalRegionDeserializer extends
        StdDeserializer<Location> {
    /**
     *
     */
    private StringToLocationConverter converter = new StringToLocationConverter();

    /**
     *
     */
    public GeographicalRegionDeserializer() {
        super(Location.class);
    }

    @Override
    public final Location deserialize(final JsonParser jsonParser,
            final DeserializationContext deserializationContext)
            throws IOException {
        return converter.convert(jsonParser.getText());
    }
}
