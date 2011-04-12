package org.emonocot.job.scratchpads;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.transform.stax.StAXSource;

import org.emonocot.job.scratchpads.model.EoLAgent;
import org.emonocot.job.scratchpads.model.EoLDataObject;
import org.emonocot.job.scratchpads.model.EoLTaxonItem;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.xstream.XStreamMarshaller;

import com.thoughtworks.xstream.io.xml.StaxDriver;


public class EoLTransferSchemaMarshallingTest  {
	EoLTransferSchemaQNameMapFactory eolTransferSchemaQNameMapFactory = new EoLTransferSchemaQNameMapFactory();
	private Unmarshaller unmarshaller;
	private String filename = "/org/emonocot/job/scratchpads/testFragment.xml";
	
	@Test
	public void testInitDriver() throws Exception {
		StaxDriver streamDriver = new StaxDriver(eolTransferSchemaQNameMapFactory.createInstance());
		unmarshaller =  new XStreamMarshaller();
		((XStreamMarshaller)unmarshaller).setStreamDriver(streamDriver);
		((XStreamMarshaller)unmarshaller).afterPropertiesSet();
	}
	
	@Test
	public void testParseTaxonItemFragment() throws Exception {
		StaxDriver streamDriver = new StaxDriver(eolTransferSchemaQNameMapFactory.createInstance());
		unmarshaller =  new XStreamMarshaller();
		((XStreamMarshaller)unmarshaller).setAnnotatedClass(EoLTaxonItem.class);
		((XStreamMarshaller)unmarshaller).setStreamDriver(streamDriver);
		((XStreamMarshaller)unmarshaller).afterPropertiesSet();
		EoLTaxonItem taxon = (EoLTaxonItem)unmarshaller.unmarshal(new StAXSource(getXMLEventReader(filename)));
		
		assertNotNull("Result from unmarshal should not be null",taxon);
		assertTrue("Object returned should be an EoLTaxon", taxon instanceof EoLTaxonItem);
		assertEquals("Identifier should be unmarshalled properly", taxon.getIdentifer(),"scratchpad:nid:1371");
		assertEquals("Source should be unmarshalled properly", taxon.getSource(),"http://scratchpad.cate-araceae.org/content/anthurium-schott");
		assertEquals("ScientificName should be unmarshalled properly", taxon.getScientificName(),"Anthurium Schott");
		
		assertNotNull("DataObjects property should not be null", taxon.getDataObjects());
		assertFalse("DataObjects property should not be empty", taxon.getDataObjects().isEmpty());
		assertEquals("DataObjects property should contain three objects",4, taxon.getDataObjects().size());
		
		EoLDataObject dataObject = taxon.getDataObjects().get(0);
		assertTrue("Object returned should be an EoLDataObject", dataObject instanceof EoLDataObject);
		assertEquals("DataType should be unmarshalled properly", dataObject.getDataType(),"http://purl.org/dc/dcmitype/Text");
		assertEquals("Created should be unmarshalled properly", dataObject.getCreated(),"1300970978");
		assertEquals("Modified should be unmarshalled properly", dataObject.getModified(),"1300970994");
		assertEquals("Title should be unmarshalled properly", dataObject.getTitle(),"General Description");
		assertEquals("License should be unmarshalled properly", dataObject.getLicense(),"http://creativecommons.org/licenses/by-nc/3.0/");
		assertEquals("Source should be unmarshalled properly", dataObject.getSource(),"http://scratchpad.cate-araceae.org/content/anthurium-schott");
		assertEquals("Subject should be unmarshalled properly", dataObject.getSubject(),"http://rs.tdwg.org/ontology/voc/SPMInfoItems#GeneralDescription");
		assertTrue("Description should not be a zero-length string", dataObject.getDescription().length() > 0);
		
		EoLAgent agent = dataObject.getAgent();
		assertNotNull("Agent should not be null", agent);
		assertTrue("Object returned should be an instance of EoLAgent",agent instanceof EoLAgent);
		assertEquals("URI should have been unmarshalled properly",agent.getURI(),"http://scratchpad.cate-araceae.org/users/ben");
		assertEquals("Role should have been unmarshalled properly", agent.getRole(),"author");
	}
	
	private static XMLEventReader getXMLEventReader(String filename) {
	    XMLInputFactory xmlif = null;
	    XMLEventReader xmlr = null;
	    try {
	      xmlif = XMLInputFactory.newInstance();
	      xmlif.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, Boolean.TRUE);
	      xmlif.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, Boolean.FALSE);
	      xmlif.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, Boolean.TRUE);
	      xmlif.setProperty(XMLInputFactory.IS_COALESCING, Boolean.TRUE);

	      Resource resource = new ClassPathResource(filename);
	      
	      xmlr = xmlif.createXMLEventReader(resource.getInputStream());
	    } catch (Exception ex) {
	      ex.printStackTrace();
	    }
	    return xmlr;
	  }
}
