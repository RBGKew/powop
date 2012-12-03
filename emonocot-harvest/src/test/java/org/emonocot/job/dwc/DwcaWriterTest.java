/**
 * 
 */
package org.emonocot.job.dwc;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.emonocot.job.dwc.write.SimpleRecord;
import org.gbif.dwc.record.Record;
import org.gbif.dwc.terms.ConceptTerm;
import org.gbif.dwc.terms.DcTerm;
import org.gbif.dwc.terms.DwcTerm;
import org.gbif.dwc.terms.GbifTerm;
import org.gbif.dwc.terms.TermFactory;

//import org.gbif.dwc.text.ArchiveWriter; //from dwca-reader v1.7.6
import org.gbif.dwc.text.Archive;
import org.gbif.dwc.text.ArchiveField;
import org.gbif.dwc.text.ArchiveFile;
import org.gbif.dwc.text.DwcaWriter;
import org.gbif.dwc.text.StarRecord;
import org.gbif.utils.file.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author jk00kg
 * 
 */
public class DwcaWriterTest {

	/**
	 * 
	 */
	private StarRecord testStar;
	
	/**
	 * 
	 */
	private Archive arch;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		// a priori knowledge of incoming data:
		// extensions

		ArrayList<String> supportedExtensions = new ArrayList<String>();

//		Declare datatypes upfront to avoid additional cost at runtime
//		supportedExtensions.add("Description");
		testStar = new StarRecord(supportedExtensions);

		SimpleRecord core = new SimpleRecord(DwcTerm.Taxon);
		core.setId("urn:example.com:Record:1");
		core.setProperty(DwcTerm.scientificName, "Dudus novitatis");
		testStar.newCoreRecord(core);
		SimpleRecord extension = new SimpleRecord(GbifTerm.Description);
		extension.setId("urn:example.com:Record:desc:1");
		extension.setProperty(DcTerm.type, "habitat");
		extension.setProperty(DcTerm.description, "Dark basements, also known in server rooms and " +
				"other localities with high socket counts");
		
		//To ensure all info goes through (as an alternative to defining 'supportedExtensions' as above
		if(!testStar.extensions().containsKey(extension.getRowType().simpleName())){
			testStar.extensions().put(extension.getRowType().simpleName(), new ArrayList<Record>());
		}

		testStar.addRecord(extension.getRowType().simpleName(), extension);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testWriteCoreFile() {
		try {
			File compileDir = FileUtils.createTempDir();
			DwcaWriter writer = new DwcaWriter(DwcTerm.Taxon, compileDir);
			writer.newRecord("urn:example.com:taxon:1");
			writer.addCoreColumn(DwcTerm.scientificName, "Aus");
			writer.newRecord("urn:example.com:taxon:2");
			writer.addCoreColumn(DwcTerm.scientificName, "Aus bus");
			writer.addCoreColumn(DwcTerm.parentNameUsage, "Aus");
			writer.addCoreColumn(DwcTerm.parentNameUsageID, "urn:example.com:taxon:1");
			writer.newRecord("urn:example.com:taxon:2");
			writer.addCoreColumn(DwcTerm.scientificName, "Aus cus");
			writer.addCoreColumn(DwcTerm.acceptedNameUsage, "Aus bus");
			writer.addCoreColumn(DwcTerm.parentNameUsageID, "urn:example.com:taxon:1");
			writer.close();
			File[] files = compileDir.listFiles();
			List<String> expectedFilenames = new ArrayList<String>();
			expectedFilenames.add("meta.xml");
			expectedFilenames.add("taxon.txt");
			for (String actualFilename : compileDir.list()) {
				assertTrue( "There shouldn't be a '" + actualFilename + "' file",
						expectedFilenames.contains(actualFilename));
				expectedFilenames.remove(actualFilename);
			}
			assertEquals("There were " + expectedFilenames.size() + " more files expected",
					0, expectedFilenames.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Test
	public void testWriteStar() {
		File compileDir;
		TermFactory termFactory = new TermFactory();

		try {
			compileDir = FileUtils.createTempDir();
			DwcaWriter writer = new DwcaWriter(DwcTerm.Taxon, compileDir);
			Record core = testStar.core();
			writer.newRecord(core.id());
			for (ConceptTerm term : core.terms()) {
				writer.addCoreColumn(term, core.value(term));
			}

			arch = new Archive();
			ArchiveFile file = new ArchiveFile();
			ArchiveField field = new ArchiveField();

			// extensions
			for (String rt : testStar.extensions().keySet()) {
				ConceptTerm rowType = termFactory.findTerm(rt);
				ArchiveFile af = arch.getExtension(rowType);
				if(af == null) {
					af = new ArchiveFile();
					arch.addExtension(af);
					af.setRowType(rt);
				}

				// iterate over records for one extension
				for (Record row : testStar.extension(rt)) {
					//Make sure all fields will be added
					for(ConceptTerm fieldTerm : row.terms()){
						if(!af.hasTerm(fieldTerm)){
							af.addField(new ArchiveField(null, fieldTerm, null, null));
						}
					}
					writer.addExtensionRecord(rowType,DwcaWriter.recordToMap(row, af));
				}
			}

			writer.close();

			//Assertions
			File[] files = compileDir.listFiles();
			List<String> expectedFilenames = new ArrayList<String>();
			expectedFilenames.add("meta.xml");
			expectedFilenames.add("taxon.txt");
			expectedFilenames.add("description.txt");
			for (String actualFilename : compileDir.list()) {
				assertTrue(
						"There shouldn't be a '" + actualFilename + "' file",
						expectedFilenames.contains(actualFilename));
				expectedFilenames.remove(actualFilename);
			}
			assertEquals("There were " + expectedFilenames.size()
					+ " more files expected", 0, expectedFilenames.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

}
