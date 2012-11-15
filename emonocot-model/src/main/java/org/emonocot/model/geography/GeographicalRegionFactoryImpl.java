package org.emonocot.model.geography;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.solr.common.SolrInputDocument;
import org.emonocot.model.Distribution;
import org.emonocot.model.Taxon;
import org.geotools.data.DataStore;
import org.geotools.data.FeatureSource;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.factory.GeoTools;
import org.geotools.feature.FeatureCollection;
import org.opengis.feature.Feature;
import org.opengis.feature.GeometryAttribute;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.identity.FeatureId;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.io.WKTWriter;

public class GeographicalRegionFactoryImpl implements GeographicalRegionFactory {
	
	private boolean spatialIndexingEnabled = false;
	
	private File baseDirectory;
	
	private DataStore level1DataStore;
	
	private DataStore level2DataStore;
	
	private DataStore level3DataStore;
	
	private FilterFactory2 filterFactory;
	
	private FeatureSource level1FeatureSource;
	
	private FeatureSource level2FeatureSource;
	
	private FeatureSource level3FeatureSource;
	
	private WKTWriter wktWriter = new WKTWriter();
	
	public void setBaseDirectory(File baseDirectory) {
		this.baseDirectory = baseDirectory;
	}
	
	public void afterPropertiesSet() throws Exception {
		ShapefileDataStoreFactory dataStoreFactory = new ShapefileDataStoreFactory();
		level3DataStore = createDataStore(dataStoreFactory, baseDirectory, "level3");
		level3FeatureSource = level3DataStore.getFeatureSource("level3");
		
		level2DataStore = createDataStore(dataStoreFactory, baseDirectory, "level2");
		level2FeatureSource = level2DataStore.getFeatureSource("level2");
		
		level1DataStore = createDataStore(dataStoreFactory, baseDirectory, "level1");
		level1FeatureSource = level1DataStore.getFeatureSource("level1");
		
		filterFactory = CommonFactoryFinder.getFilterFactory2(GeoTools.getDefaultHints());
	}
	
	public void destroy() {
		level1DataStore.dispose();
		level2DataStore.dispose();
		level3DataStore.dispose();
	}
	
	private DataStore createDataStore(ShapefileDataStoreFactory dataStoreFactory, File baseDirectory, String string) throws Exception {
		File shapeFile = new File(baseDirectory,string);
		Map<String, Serializable> params = new HashMap<String, Serializable>();
        params.put("url", shapeFile.toURI().toURL());
        params.put("create spatial index", Boolean.TRUE);
		return dataStoreFactory.createDataStore(params);
	}

	@Override
	public void indexSpatial(Taxon taxon, SolrInputDocument sid) throws Exception {
		Geometry geometry = null;
		if (spatialIndexingEnabled && taxon.getDistribution().size() > 1) {
			// for the moment assume non-contiguous
			List<Polygon> polygons = new ArrayList<Polygon>();
			GeometryFactory geometryFactory = null;
			for (Distribution d : taxon.getDistribution()) {
				Geometry g = getGeometry(d.getLocation());
				geometryFactory = g.getFactory();
				String geometryType = g.getGeometryType();
				if (geometryType == "Polygon") {
					polygons.add((Polygon) g);
				} else if (geometryType == "MultiPolygon") {
					for (int i = 0; i < g.getNumGeometries(); i++) {
						polygons.add((Polygon) g.getGeometryN(i));
					}

				}
			}
			geometry = new MultiPolygon(polygons.toArray(new Polygon[polygons.size()]),	geometryFactory);
			sid.addField("geo", wktWriter.write(geometry));
		} else if (spatialIndexingEnabled && taxon.getDistribution().size() == 1) {
			geometry = getGeometry(taxon.getDistribution().iterator().next().getLocation());
			sid.addField("geo", wktWriter.write(geometry));
		} else {
			
		}
	}
	
	/**
	 * @return the spatialIndexingEnabled
	 */
	public boolean isSpatialIndexingEnabled() {
		return spatialIndexingEnabled;
	}

	/**
	 * @param spatialIndexingEnabled the spatialIndexingEnabled to set
	 */
	public void setSpatialIndexingEnabled(boolean spatialIndexingEnabled) {
		this.spatialIndexingEnabled = spatialIndexingEnabled;
	}

	private Geometry getGeometry(GeographicalRegion geographicalRegion) throws Exception {
		if(geographicalRegion == null) {
			return null;
		} else {
			Set<FeatureId> fids = new HashSet<FeatureId>();
			
			FeatureCollection featureCollection = null;
			if(geographicalRegion.getLevel() == 0) {	
				fids.add(filterFactory.featureId("level1." + geographicalRegion.getFeatureId()));
				Filter filter = filterFactory.id(fids);
				featureCollection = level1FeatureSource.getFeatures(filter);
			} else if(geographicalRegion.getLevel() == 1) {
				fids.add(filterFactory.featureId("level2." + geographicalRegion.getFeatureId()));
				Filter filter = filterFactory.id(fids);
				featureCollection = level2FeatureSource.getFeatures(filter);
			} else if(geographicalRegion.getLevel() == 2) {
				fids.add(filterFactory.featureId("level3." + geographicalRegion.getFeatureId()));
				Filter filter = filterFactory.id(fids);
				featureCollection = level3FeatureSource.getFeatures(filter);
			}
			Feature feature = featureCollection.features().next();
			GeometryAttribute geometryAttribute = feature.getDefaultGeometryProperty();
			return (Geometry)feature.getDefaultGeometryProperty().getValue();
		}
	}

	@Override
	public void indexSpatial(Place p, SolrInputDocument sid) throws Exception {
		if (spatialIndexingEnabled && p.getShape() != null) {
			sid.addField("geo", wktWriter.write(p.getShape()));
		}
	}

	@Override
	public void indexSpatial(Point p, SolrInputDocument sid) throws Exception {
		if (spatialIndexingEnabled && p != null) {
			sid.addField("geo", wktWriter.write(p));
		}
	}

}
