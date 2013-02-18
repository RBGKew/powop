package org.emonocot.job.map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.emonocot.model.constants.Location;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.configuration.JobLocator;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

/**
 *
 * @author ben
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
    "/META-INF/spring/batch/jobs/printMap.xml",
    "/META-INF/spring/applicationContext-test.xml" })
public class PrintMapIntegrationTest {

    private Logger logger = LoggerFactory.getLogger(
            PrintMapIntegrationTest.class);

    @Autowired
    private JobLocator jobLocator;

    @Autowired
    private JobLauncher jobLauncher;
    
    @Autowired
    private ObjectMapper objectMapper;

    /**
     *
     * @param polygon 
     * @throws IOException
     *             if a temporary file cannot be created.
     * @throws NoSuchJobException
     *             if SpeciesPageHarvestingJob cannot be located
     * @throws JobParametersInvalidException
     *             if the job parameters are invalid
     * @throws JobInstanceAlreadyCompleteException
     *             if the job has already completed
     * @throws JobRestartException
     *             if the job cannot be restarted
     * @throws JobExecutionAlreadyRunningException
     *             if the job is already running
     */

    private JobExecution createMap(String mapName, String layerName, String featureId, double centerX, double centerY, double scale, Polygon polygon) throws Exception {
    	Map<String, JobParameter> parameters = new HashMap<String, JobParameter>();
    	File output = new File(mapName);
		parameters.put("output.file", new JobParameter(output.getAbsolutePath()));
		MapSpec mapSpec = new MapSpec();
		mapSpec.setDpi(300);
		
		TMSLayer tmsLayer = new TMSLayer();
		tmsLayer.setLayer("eMonocot");
		tmsLayer.setBaseURL("http://e-monocot.org/tiles/");
		tmsLayer.setMaxExtent(new double[] {-20037508.3392,-20037508.3392,20037508.3392,20037508.3392});
		tmsLayer.setResolutions(new double[] {156543.0339,78271.51695,39135.758475,19567.8792375,9783.93961875,4891.969809375,2445.9849046875});
		tmsLayer.setFormat("png");
		mapSpec.getLayers().add(tmsLayer);
		WMSLayer wmsLayer = new WMSLayer();
		wmsLayer.setLayers(new String[] {layerName});
		wmsLayer.setBaseURL("http://e-monocot.org/geoserver/wms");
		wmsLayer.setMaxExtent(new double[] {-20037508.3392,-20037508.3392,20037508.3392,20037508.3392});
		wmsLayer.setResolutions(new double[] {156543.0339,78271.51695,39135.758475,19567.8792375,9783.93961875,4891.969809375,2445.9849046875});
		wmsLayer.setFormat("image/png");
		wmsLayer.setStyles(new String[] {"eMonocot"});
		wmsLayer.getCustomParams().put("featureid",featureId);
		mapSpec.getLayers().add(wmsLayer);
		Page page = new Page();
		page.setBbox(new double[] {polygon.getCoordinates()[0].x,polygon.getCoordinates()[0].y,polygon.getCoordinates()[2].x,polygon.getCoordinates()[2].y});
		
		mapSpec.getPages().add(page);
		//"{\"layout\": \"A4 portrait\",\"title\": \"A simple example\",\"srs\": \"EPSG:3857\",\"dpi\": 300,\"units\": \"m\",\"outputFormat\": \"png\",\"layers\": [{\"type\": \"TMS\",\"layer\": \"eMonocot\",\"baseURL\": \"http://e-monocot.org/tiles/\",\"maxExtent\": [-20037508.3392,-20037508.3392,20037508.3392,20037508.3392],\"tileSize\": [256, 256],\"resolutions\": [156543.0339,78271.51695,39135.758475,19567.8792375,9783.93961875,4891.969809375,2445.9849046875],\"format\": \"png\"},{\"type\": \"WMS\",\"layers\": [\"tdwg:level3\"],\"baseURL\": \"http://e-monocot.org/geoserver/wms\",\"maxExtent\": [-20037508.3392,-20037508.3392,20037508.3392,20037508.3392],\"tileSize\": [256, 256],\"resolutions\": [156543.0339,78271.51695,39135.758475,19567.8792375,9783.93961875,4891.969809375,2445.9849046875],\"format\": \"image/png\",\"styles\": [\"eMonocot\"],\"customParams\":{\"featureid\":\"266,144,196,365,302\"}}],\"pages\": [{\"center\": [731000,5864000],\"scale\": 8000000,\"mapTitle\": \"First map\",\"comment\": \"This is the first page selected by the user.\",\"rotation\": 0}]}"

		parameters.put("json", new JobParameter(objectMapper.writeValueAsString(mapSpec)));
		

		JobParameters jobParameters = new JobParameters(parameters);
		Job archiveCreatorJob = jobLocator.getJob("PrintMap");
		assertNotNull("PrintMap must exist", archiveCreatorJob);
		JobExecution jobExecution = jobLauncher.run(archiveCreatorJob, jobParameters);
		return jobExecution;
		        
    }
    
    @Test
    public void testMakeContinentMaps() throws Exception {
    	Location[] continents = new Location[] {
    			Location.AFRICA,
    			Location.EUROPE,
    			Location.ANTARCTIC_CONTINENT,
    			Location.AUSTRALASIA,
    			Location.ASIA_TEMPERATE,
    			Location.ASIA_TROPICAL,
    			Location.NORTHERN_AMERICA,
    			Location.SOUTHERN_AMERICA
    	};
    	/*for(Location continent : continents) {
    		testMakeMap(continent);
    		for(Location region : continent.getChildren()) {
    		    testMakeMap(region);
    		}
    	}*/
    	testMakeMap(Location.NORTHERN_AMERICA);
    	
    }
    
    public void testMakeMap(Location location) throws Exception {
    	
    	String mapName = "target/" +location.name() +".png";
    	String layer = "tdwg:level" + (location.getLevel() + 1);
    	

    	JobExecution jobExecution = createMap(mapName, layer,location.getFeatureId().toString(),0.0,0.0,0.0, location.getEnvelope());
    	assertEquals("The Job should be sucessful", ExitStatus.COMPLETED, jobExecution.getExitStatus());
    }
    
}
