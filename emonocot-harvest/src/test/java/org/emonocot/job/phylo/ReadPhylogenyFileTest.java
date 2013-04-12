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
import org.forester.phylogeny.data.BranchData;
import org.forester.phylogeny.data.NodeData;
import org.forester.phylogeny.data.PropertiesMap;
import org.forester.phylogeny.data.Property;
import org.forester.phylogeny.data.Property.AppliesTo;
import org.forester.phylogeny.data.Uri;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class ReadPhylogenyFileTest {

	Resource tree = new ClassPathResource("/org/emonocot/job/phylo/test.nwk");

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


		StringBuffer stringBuffer = phylogenyWriter.toPhyloXML(phylogeny, 1);
		System.out.println(stringBuffer.toString());

	}

}