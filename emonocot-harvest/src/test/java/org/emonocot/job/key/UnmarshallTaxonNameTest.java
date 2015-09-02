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
package org.emonocot.job.key;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.xml.transform.stax.StAXSource;

import org.junit.Test;
import org.tdwg.ubif.TaxonName;

/**
 *
 * @author ben
 *
 */
public class UnmarshallTaxonNameTest extends
AbstractIdentificationKeyMarshallingTest {
	/**
	 *
	 */
	String filename
	= "/org/emonocot/job/sdd/TestKey_taxonNameFragment.xml";

	/**
	 * @throws Exception
	 *             if there is a problem
	 *
	 */
	@Test
	public final void testParseTaxonItemFragment() throws Exception {

		TaxonName taxonName = (TaxonName) super.getUnmarshaller()
				.unmarshal(new StAXSource(getXMLEventReader(filename)));
		assertNotNull("taxonName should not be null", taxonName);
		assertEquals("taxonName.id should equal t2", "t2", taxonName.getId());
		assertEquals("taxonName.representation.label should be Arum alpinariae", "Arum alpinariae", taxonName.getRepresentation().getLabel());
		assertEquals("size(taxonName.representation.mediaObject) should be 2", 2, taxonName.getRepresentation().getMediaObjects().size());
	}
}
