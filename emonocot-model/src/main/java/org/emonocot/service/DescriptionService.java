package org.emonocot.service;

import org.emonocot.model.description.Content;
import org.emonocot.model.description.Feature;
import org.emonocot.model.description.TextContent;
import org.emonocot.model.taxon.Taxon;

/**
 *
 * @author ben
 *
 */
public interface DescriptionService extends Service<Content> {

    /**
     *
     * @param feature The subject of the text content.
     * @param taxon The taxonomic subject of the text content.
     * @return A text content object, or null if no such object exists.
     */
    TextContent getTextContent(Feature feature, Taxon taxon);

}
