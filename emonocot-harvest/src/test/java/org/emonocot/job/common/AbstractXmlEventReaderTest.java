/*
 * This is eMonocot, a global online biodiversity information resource.
 *
 * Copyright © 2011–2015 The Board of Trustees of the Royal Botanic Gardens, Kew and The University of Oxford
 *
 * eMonocot is free software: you can redistribute it and/or modify it under the terms of the
 * GNU Affero General Public License as published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * eMonocot is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * The complete text of the GNU Affero General Public License is in the source repository as the file
 * ‘COPYING’.  It is also available from <http://www.gnu.org/licenses/>.
 */
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