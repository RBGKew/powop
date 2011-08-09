package org.emonocot.job.common;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/**
 *
 * @author ben
 *
 */
public abstract class AbstractXmlEventReaderTest {

    /**
     *
     * @param filename The filename of the XML resource
     * @return An XMLEventReader
     */
    protected static XMLEventReader getXMLEventReader(final String filename) {
        XMLInputFactory xmlif = null;
        XMLEventReader xmlr = null;
        try {
          xmlif = XMLInputFactory.newInstance();
            xmlif.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES,
                    Boolean.TRUE);
            xmlif.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES,
                    Boolean.FALSE);
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