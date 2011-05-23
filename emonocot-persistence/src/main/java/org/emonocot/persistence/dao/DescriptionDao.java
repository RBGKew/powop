package org.emonocot.persistence.dao;

import org.emonocot.model.description.Content;
import org.emonocot.model.description.Feature;
import org.emonocot.model.description.TextContent;
import org.emonocot.model.taxon.Taxon;

public interface DescriptionDao extends Dao<Content> {
    
    TextContent getTextContent(Feature feature, Taxon taxon);

}
