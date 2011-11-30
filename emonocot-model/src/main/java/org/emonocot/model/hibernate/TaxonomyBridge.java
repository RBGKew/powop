package org.emonocot.model.hibernate;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.emonocot.model.media.Image;
import org.emonocot.model.taxon.Taxon;
import org.hibernate.search.bridge.FieldBridge;
import org.hibernate.search.bridge.LuceneOptions;

/**
 * Method of adding the "family" facet to images.
 * Could be extended to include other objects such as specimens
 * which should be considered to be related to a taxonomic family
 *
 * @author ben
 *
 */
public class TaxonomyBridge implements FieldBridge {

    /**
     * @param name Set the name of the field
     * @param value Set the value to be indexed
     * @param document Set the lucene document
     * @param luceneOptions Set the options for indexing
     */
    public final void set(final String name, final Object value,
            final Document document, final LuceneOptions luceneOptions) {
        Image image = (Image) value;
        Taxon taxon = image.getTaxon();
        if (taxon != null && taxon.getFamily() != null
                && taxon.getFamily().trim().length() > 0) {
            Field familyField = new Field("family",
                    taxon.getFamily(), luceneOptions.getStore(),
                    luceneOptions.getIndex(),
                    luceneOptions.getTermVector());
            familyField.setBoost(luceneOptions.getBoost());
            document.add(familyField);
        }
    }

}
