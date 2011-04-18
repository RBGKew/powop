package org.emonocot.service;

import org.emonocot.model.description.Feature;
import org.emonocot.model.description.TextContent;
import org.emonocot.model.taxon.Taxon;

public interface DescriptionService {

	TextContent getTextContent(Feature feature, Taxon taxon);

}
