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
package org.emonocot.portal.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.easymock.EasyMock;
import org.emonocot.api.TaxonService;
import org.emonocot.model.IdentificationKey;
import org.emonocot.model.Taxon;
import org.emonocot.portal.controller.ClassificationController.Node;
import org.junit.Before;
import org.junit.Test;

public class ClassificationControllerTest {

	private ClassificationController classificationController;

	private TaxonService taxonService;

	private List<Taxon> taxa;

	@Before
	public void setUp() {
		classificationController = new ClassificationController();
		taxonService = EasyMock.createMock(TaxonService.class);
		classificationController.setTaxonService(taxonService);
		taxa =  new ArrayList<Taxon>();
		Taxon taxon = new Taxon();
		IdentificationKey identificationKey = new IdentificationKey();
		identificationKey.setIdentifier("identificationKey123");
		identificationKey.setTitle("keyTitle");
		identificationKey.setId(1L);
		taxon.getKeys().add(identificationKey);
		taxa.add(taxon);
	}

	@Test
	public void testKeyUrl() {
		EasyMock.expect(taxonService.loadChildren(EasyMock.eq("test"), (Integer)EasyMock.isNull(), (Integer)EasyMock.isNull(), EasyMock.eq("classification-tree"))).andReturn(taxa);

		EasyMock.replay(taxonService);
		List<Node> nodes = classificationController.getTaxonTreeNode("test");
		EasyMock.verify(taxonService);

		assertNotNull("Nodes should not be null",nodes);
		assertEquals("There should be one node", nodes.size(), 1);
		assertTrue("The first node should contain a data-key-link attribute", ((Map)nodes.get(0).getData().get("attr")).containsKey("data-key-link"));
		assertEquals("The value of the data-key-link attribute should be keyTitle:::key/1",((Map)nodes.get(0).getData().get("attr")).get("data-key-link"),"keyTitle:::key/1");
	}

}
