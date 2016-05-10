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
package org.emonocot.job.phylo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.forester.io.parsers.PhylogenyParser;
import org.forester.io.parsers.util.ParserUtils;
import org.forester.io.writers.PhylogenyWriter;
import org.forester.phylogeny.Phylogeny;
import org.forester.phylogeny.PhylogenyMethods;
import org.forester.phylogeny.PhylogenyNode;
import org.forester.phylogeny.data.Annotation;
import org.forester.phylogeny.data.PhylogenyDataUtil;
import org.forester.phylogeny.data.Uri;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class ReadPhylogenyFileTest {

	Resource tree = new ClassPathResource("/__files/test.nwk");

	private static Logger logger = LoggerFactory.getLogger(ReadPhylogenyFileTest.class);

	@Test
	public void readFile() throws Exception {

		File treefile = tree.getFile();
		PhylogenyParser parser = ParserUtils.createParserDependingOnFileType(treefile, true);
		Phylogeny[] phylogenies = PhylogenyMethods.readPhylogenies(parser, treefile);
		Phylogeny phylogeny = phylogenies[0];


		PhylogenyWriter phylogenyWriter = PhylogenyWriter.createPhylogenyWriter();
		PhylogenyNode node = phylogeny.getRoot();
		Annotation annotation = new Annotation();
		annotation.setDesc("Base of many coffees");
		List<Uri> uris = new ArrayList<Uri>();
		uris.add(new Uri("http://en.wikipedia.org/wiki/Espresso",null,null));
		annotation.setUris(uris);
		node.getNodeData().addAnnotation(annotation);
		addBranchLengths(phylogeny.getRoot());
		StringBuffer stringBuffer = phylogenyWriter.toPhyloXML(phylogeny, 1);
		logger.debug(stringBuffer.toString().replaceAll("\r\n", ""));

		logger.debug(phylogeny.getNumberOfExternalNodes() + " " + phylogeny.getHeight());

	}

	private void addBranchLengths(PhylogenyNode node) {
		if(node.isRoot() || node.getDistanceToParent() != PhylogenyDataUtil.BRANCH_LENGTH_DEFAULT) {
			// do nothing
		} else {
			node.setDistanceToParent(1.0D);
		}
		for(PhylogenyNode descendant : node.getDescendants()) {
			addBranchLengths(descendant);
		}
	}

}
