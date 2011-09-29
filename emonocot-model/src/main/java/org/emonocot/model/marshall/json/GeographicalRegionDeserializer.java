package org.emonocot.model.marshall.json;

import java.io.IOException;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.deser.StdDeserializer;
import org.emonocot.model.geography.GeographicalRegion;
import org.emonocot.model.geography.GeographyConverter;

/**
 *
 * @author ben
 *
 */
public class GeographicalRegionDeserializer extends
        StdDeserializer<GeographicalRegion> {
    /**
     *
     */
    private GeographyConverter converter = new GeographyConverter();

    /**
     *
     */
    public GeographicalRegionDeserializer() {
        super(GeographicalRegion.class);
    }

    @Override
    public final GeographicalRegion deserialize(final JsonParser jsonParser,
            final DeserializationContext deserializationContext)
            throws IOException {
        return converter.convert(jsonParser.getText());
    }
}
