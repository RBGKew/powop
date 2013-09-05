package org.emonocot.model.marshall.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.emonocot.model.constants.Location;
import org.emonocot.model.convert.StringToLocationConverter;

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
