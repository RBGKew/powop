/**
 * 
 */
package org.emonocot.job.dwc;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.easymock.EasyMock;
import org.gbif.dwc.record.Record;
import org.gbif.dwc.terms.ConceptTerm;
import org.gbif.dwc.terms.DcTerm;
import org.gbif.dwc.terms.DwcTerm;
import org.gbif.dwc.terms.GbifTerm;
import org.gbif.dwc.text.DwcaWriter;
import org.gbif.dwc.text.StarRecord;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * @author jk00kg
 *
 */
@Ignore
public class StarRecordWriterTest {

    private StarRecord star;
	private Map<ConceptTerm, String> expectedExtensionRecords;

	/**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
    	star = new StarRecord(new ArrayList<String>());
		ArrayList<String> supportedExtensions = new ArrayList<String>();

//		Declare datatypes upfront to avoid additional cost at runtime
//		supportedExtensions.add("Description");
		star = new StarRecord(supportedExtensions);

		SimpleRecord core = new SimpleRecord(DwcTerm.Taxon);
		core.setId("urn:example.com:Record:1");
		core.setProperty(DwcTerm.scientificName, "Dudus novitatis");
		star.newCoreRecord(core);
		SimpleRecord extension = new SimpleRecord(GbifTerm.Description);
		extension.setId("urn:example.com:Record:desc:1");
		extension.setProperty(DcTerm.type, "habitat");
		extension.setProperty(DcTerm.description, "Dark basements, also known in server rooms and " +
				"other localities with high socket counts");
		//To ensure all info goes through (as an alternative to defining 'supportedExtensions' as above
		if(!star.extensions().containsKey(extension.getRowType().simpleName())){
			star.extensions().put(extension.getRowType().simpleName(), new ArrayList<Record>());
		}

		star.addRecord(extension.getRowType().simpleName(), extension);
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    /**
     * Test method for {@link org.emonocot.job.dwc.StarRecordWriter#write(java.util.List)}.
     * @throws Exception 
     */
    @Test
    public void testWrite() throws Exception {
    	DwcaWriter dwcaWriter = EasyMock.createMock(DwcaWriter.class);
    	//expect
    	dwcaWriter.newRecord(star.core().id());
    	dwcaWriter.addCoreColumn(DwcTerm.scientificName, "Dudus novitatis");
    	dwcaWriter.addExtensionRecord(DcTerm.description, expectedExtensionRecords);
    	EasyMock.replay(dwcaWriter);
    	StarRecordWriter writer = new StarRecordWriter();
		writer.setWriter(dwcaWriter);
    	List<StarRecord> items = new ArrayList<StarRecord>();
    	items.add(star);
		writer.write(items);
		EasyMock.verify(dwcaWriter);
//    	TODO write assertions 
        for (int i = 0; i < 20; i++) {
            System.err.println("Write the test and stop the pain!!! " + this.toString());
        }
    }

}
