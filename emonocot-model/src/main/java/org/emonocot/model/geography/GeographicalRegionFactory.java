package org.emonocot.model.geography;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
import com.vividsolutions.jts.io.WKTWriter;

public class GeographicalRegionFactory {
	
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



	String getWKT(GeographicalRegion geographicalRegion) throws Exception {
		if(geographicalRegion == null) {
			return null;
		} else {
			Set<FeatureId> fids = new HashSet<FeatureId>();
			System.out.println(geographicalRegion.getFeatureId().toString());
			
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
			return wktWriter.write((Geometry)feature.getDefaultGeometryProperty().getValue());
		}
	}

}
