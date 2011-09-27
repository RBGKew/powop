package org.emonocot.model.marshall.json;

import java.io.IOException;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.DeserializationContext;
import org.emonocot.model.taxon.Taxon;

/**
 *
 * @author ben
 *
 */
public class TaxonDeserializer extends BaseDeserializer<Taxon> {

    /**
     *
     */
    public TaxonDeserializer() {
        super(Taxon.class);
    }

    @Override
    public final Taxon deserialize(final JsonParser jsonParser,
            final DeserializationContext deserializationContext)
            throws IOException {
        String identifier = jsonParser.getText();
        return service.load(identifier, "taxon-page");
    }

}
