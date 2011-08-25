package org.emonocot.persistence.dao;

import org.emonocot.model.description.Content;
import org.emonocot.model.description.Feature;
import org.emonocot.model.description.TextContent;
import org.emonocot.model.taxon.Taxon;

/**
 *
 * @author ben
 *
 */
public interface DescriptionDao extends Dao<Content> {

    /**
     *
     * @param feature Set the feature
     * @param taxon Set the taxon
     * @return some text content
     */
    TextContent getTextContent(Feature feature, Taxon taxon);

}
