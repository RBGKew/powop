package org.emonocot.portal.view;

import org.emonocot.model.description.TextContent;
import org.emonocot.model.description.Feature;
import org.emonocot.model.taxon.Taxon;

/**
 *
 * @author ben
 *
 */
public final class Functions {
    /**
     *
     */
    private Functions() {
    }

    /**
     *
     * @return
     */
    public static Feature[] features() {
        return Feature.values();
    }

    /**
     *
     * @param taxon Set the taxon
     * @param feature Set the feature
     * @return a Content object, or null
     */
    public static TextContent content(Taxon taxon, Feature feature) {
        return (TextContent)taxon.getContent().get(feature);
    }
}
