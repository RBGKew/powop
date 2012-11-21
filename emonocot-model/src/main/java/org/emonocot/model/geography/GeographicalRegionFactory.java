package org.emonocot.model.geography;

import org.apache.solr.common.SolrInputDocument;
import org.emonocot.model.Taxon;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;

public interface GeographicalRegionFactory {

	public abstract void indexSpatial(Taxon t, SolrInputDocument sid) throws Exception;
	
	public abstract void indexSpatial(Place p, SolrInputDocument sid) throws Exception;
	
	public abstract void indexSpatial(Point p, SolrInputDocument sid) throws Exception;
	
	public Geometry getGeometry(GeographicalRegion geographicalRegion) throws Exception;
	
	void setSpatialIndexingEnabled(boolean spatialIndexingEnabled);
}