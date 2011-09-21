package org.emonocot.persistence.dao.hibernate;

import org.apache.lucene.search.Query;
import org.hibernate.search.query.dsl.impl.AbstractFacet;

/**
 *
 * @author ben
 *
 */
public class FakeFacet extends AbstractFacet {

    /**
     *
     * @param facetingName Set the faceting name
     * @param fieldName Set the field name
     * @param value Set the value
     * @param count Set the count
     */
    public FakeFacet(final String facetingName, final String fieldName,
            final String value, final int count) {
        super(facetingName, fieldName, value, count);
    }

    @Override
    public final Query getFacetQuery() {
        throw new UnsupportedOperationException(
                "This class is only for returning results, not for using in a query");
    }
}
