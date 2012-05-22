package org.emonocot.model.hibernate;

import java.io.IOException;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.DocIdSet;
import org.apache.lucene.search.Filter;
/*import org.apache.lucene.spatial.base.context.SpatialContext;
import org.apache.lucene.spatial.base.context.SpatialContextProvider;
import org.apache.lucene.spatial.base.prefix.GeohashSpatialPrefixGrid;
import org.apache.lucene.spatial.base.query.SpatialArgsParser;
import org.apache.lucene.spatial.strategy.SimpleSpatialFieldInfo;
import org.apache.lucene.spatial.strategy.SpatialStrategy;
import org.apache.lucene.spatial.strategy.prefix.DynamicPrefixStrategy;*/
import org.hibernate.search.annotations.Factory;
import org.hibernate.search.annotations.Key;
import org.hibernate.search.filter.FilterKey;
import org.hibernate.search.filter.StandardFilterKey;

/**
 *
 * @author ben
 *
 */
public class SpatialFilterFactory {

    /**
     *
     */
    private int levels;

    /**
     *
     */
    private String field;

    /**
     *
     */
    private String query;

    /**
     *
     * @return a lucene filter
     */
    @Factory
    public final Filter getFilter() {
        //SpatialStrategy<SimpleSpatialFieldInfo> spatialStrategy
        //    = new DynamicPrefixStrategy(
        //            new GeohashSpatialPrefixGrid(DistributionBridge.SPATIAL_CONTEXT, levels));
        //SpatialArgsParser spatialArgsParser = new SpatialArgsParser();

        //return spatialStrategy.makeFilter(
        //        spatialArgsParser.parse(query, DistributionBridge.SPATIAL_CONTEXT),
        //        new SimpleSpatialFieldInfo(field));
        return new NullFilter();
    }
    
    public class NullFilter extends Filter {

        @Override
        public DocIdSet getDocIdSet(IndexReader indexReader) throws IOException {
            return DocIdSet.EMPTY_DOCIDSET;
        }
        
    }

    /**
     *
     * @param newLevels Set the number of levels
     */
    public final void setLevels(final int newLevels) {
        this.levels = newLevels;
    }

    /**
     *
     * @param newField Set the field name
     */
    public final void setField(final String newField) {
        this.field = newField;
    }

    /**
     *
     * @param spatialQuery Set the spatial query
     */
    public final void setQuery(final String spatialQuery) {
        this.query = spatialQuery;
    }

    /**
     *
     * @return A filter key for use in caching
     */
    @Key
    public final FilterKey getKey() {
        StandardFilterKey key = new StandardFilterKey();
        key.addParameter(levels);
        key.addParameter(field);
        key.addParameter(query);
        return key;
    }
}
