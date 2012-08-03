package org.emonocot.model.marshall.json;

import java.io.IOException;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.DeserializationContext;
import org.emonocot.model.taxon.Taxon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author ben
 *
 */
public class TaxonDeserializer extends BaseDeserializer<Taxon> {
	private static Logger logger = LoggerFactory.getLogger(TaxonDeserializer.class);

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
        if (service != null) {
        	Taxon t = service.load(identifier, "taxon-page");
        	logger.debug("service is not null, returning " + t + " for identifier " + identifier);
            return t;
        } else {
        	logger.debug("service is null, returning null");
        	return null;
        }
    }

}
