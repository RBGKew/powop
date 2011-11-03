package org.emonocot.api;

import org.emonocot.model.description.Feature;
import org.emonocot.model.description.TextContent;
import org.emonocot.model.taxon.Taxon;

/**
 *
 * @author ben
 *
 */
public interface DescriptionService extends Service<TextContent> {

    /**
     *
     * @param feature The subject of the text content.
     * @param taxon The taxonomic subject of the text content.
     * @return A text content object, or null if no such object exists.
     */
    TextContent getTextContent(Feature feature, Taxon taxon);

}
