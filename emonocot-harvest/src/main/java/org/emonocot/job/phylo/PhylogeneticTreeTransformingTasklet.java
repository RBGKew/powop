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

import org.emonocot.api.PhylogeneticTreeService;
import org.emonocot.api.TaxonService;
import org.emonocot.api.match.Match;
import org.emonocot.api.match.taxon.TaxonMatcher;
import org.emonocot.harvest.common.AbstractRecordAnnotator;
import org.emonocot.model.PhylogeneticTree;
import org.emonocot.model.Taxon;
import org.emonocot.model.constants.AnnotationCode;
import org.emonocot.model.constants.AnnotationType;
import org.emonocot.model.constants.RecordType;
import org.emonocot.persistence.hibernate.SolrIndexingListener;
import org.forester.io.parsers.PhylogenyParser;
import org.forester.io.parsers.util.ParserUtils;
import org.forester.io.writers.PhylogenyWriter;
import org.forester.phylogeny.Phylogeny;
import org.forester.phylogeny.PhylogenyMethods;
import org.forester.phylogeny.PhylogenyNode;
import org.forester.phylogeny.data.Annotation;
import org.forester.phylogeny.data.PhylogenyDataUtil;
import org.forester.phylogeny.data.Uri;
import org.gbif.ecat.parser.UnparsableException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;

/**
 *
 * @author ben
 *
 */
public class PhylogeneticTreeTransformingTasklet extends AbstractRecordAnnotator implements Tasklet {

	Logger logger = LoggerFactory.getLogger(PhylogeneticTreeTransformingTasklet.class);

	private String treeIdentifier;

	private Resource inputFile;

	private String baseUri;

	private TaxonMatcher taxonMatcher;

	private String rootTaxonIdentifier;

	private String phylogenyTitle;

	private String phylogenyCreator;

	private String phylogenyDescription;

	private String phylogenyRights;

	private String phylogenyRightsHolder;

	private String phylogenyLicense;

	private PhylogeneticTreeService phylogeneticTreeService;

	private TaxonService taxonService;

	private SolrIndexingListener solrIndexingListener;

	public void setTreeIdentifier(String treeIdentifier) {
		this.treeIdentifier = treeIdentifier;
	}

	public void setRootTaxonIdentifier(String rootTaxonIdentifier) {
		this.rootTaxonIdentifier = rootTaxonIdentifier;
	}

	public void setPhylogenyCreator(String phylogenyCreator) {
		this.phylogenyCreator = phylogenyCreator;
	}

	public void setPhylogenyDescription(String phylogenyDescription) {
		this.phylogenyDescription = phylogenyDescription;
	}

	public void setPhylogenyRights(String phylogenyRights) {
		this.phylogenyRights = phylogenyRights;
	}

	public void setPhylogenyRightsHolder(String phylogenyRightsHolder) {
		this.phylogenyRightsHolder = phylogenyRightsHolder;
	}

	public void setPhylogenyLicense(String phylogenyLicense) {
		this.phylogenyLicense = phylogenyLicense;
	}

	public void setInputFile(Resource inputFile) {
		this.inputFile = inputFile;
	}

	public void setBaseUri(String baseUri) {
		this.baseUri = baseUri;
	}

	public void setPhylogenyTitle(String phylogenyTitle) {
		this.phylogenyTitle = phylogenyTitle;
	}

	@Autowired
	public void setTaxonMatcher(TaxonMatcher taxonMatcher) {
		this.taxonMatcher = taxonMatcher;
	}

	@Autowired
	public void setPhylogeneticTreeService(PhylogeneticTreeService phylogeneticTreeService) {
		this.phylogeneticTreeService = phylogeneticTreeService;
	}

	@Autowired
	public void setTaxonService(TaxonService taxonService) {
		this.taxonService = taxonService;
	}

	@Autowired
	public void setSolrIndexingListener(SolrIndexingListener solrIndexingListener) {
		this.solrIndexingListener = solrIndexingListener;
	}

	@Override
	public RepeatStatus execute(StepContribution contribution,
			ChunkContext chunkContext) throws Exception {
		File treefile = inputFile.getFile();
		PhylogenyParser parser = ParserUtils.createParserDependingOnFileType(treefile, true);
		Phylogeny[] phylogenies = PhylogenyMethods.readPhylogenies(parser, treefile);

		Phylogeny phylogeny = phylogenies[0];

		PhylogenyWriter phylogenyWriter = PhylogenyWriter.createPhylogenyWriter();
		phylogenyWriter.setIndentPhyloxml(false);
		PhylogenyNode node = phylogeny.getRoot();
		PhylogeneticTree phylogeneticTree = phylogeneticTreeService.find(treeIdentifier);
		if(phylogeneticTree == null) {
			phylogeneticTree = new PhylogeneticTree();
			phylogeneticTree.setAuthority(getSource());
			phylogeneticTree.setIdentifier(treeIdentifier);
			org.emonocot.model.Annotation annotation = new org.emonocot.model.Annotation();
			annotation.setJobId(stepExecution.getJobExecutionId());
			annotation.setAnnotatedObj(phylogeneticTree);
			annotation.setRecordType(RecordType.PhylogeneticTree);
			annotation.setCode(AnnotationCode.Create);
			annotation.setAuthority(getSource());
			annotation.setType(AnnotationType.Info);
			phylogeneticTree.getAnnotations().add(annotation);
		} else {
			org.emonocot.model.Annotation annotation = new org.emonocot.model.Annotation();
			annotation.setJobId(stepExecution.getJobExecutionId());
			annotation.setAnnotatedObj(phylogeneticTree);
			annotation.setRecordType(RecordType.PhylogeneticTree);
			annotation.setCode(AnnotationCode.Update);
			annotation.setType(AnnotationType.Info);
			annotation.setAuthority(getSource());
			phylogeneticTree.getAnnotations().add(annotation);
			phylogeneticTree.getLeaves().clear();
		}

		addTaxonLinks(node,phylogeneticTree);
		boolean hasBranchLengths = addBranchLengths(node);

		StringBuffer stringBuffer = phylogenyWriter.toPhyloXML(phylogeny, 1);

		phylogeneticTree.setNumberOfExternalNodes(new Long(phylogeny.getNumberOfExternalNodes()));
		phylogeneticTree.setHasBranchLengths(hasBranchLengths);

		if(phylogeneticTree.getTitle() == null || phylogeneticTree.getTitle().isEmpty()) {
			phylogeneticTree.setTitle(phylogeny.getName());
		}
		if(phylogeneticTree.getDescription() == null || phylogeneticTree.getDescription().isEmpty()) {
			phylogeneticTree.setDescription(phylogeny.getDescription());
		}
		if(phylogenyTitle != null && !phylogenyTitle.isEmpty()) {
			phylogeneticTree.setTitle(phylogenyTitle);
		}
		if(phylogenyDescription != null && !phylogenyDescription.isEmpty()) {
			phylogeneticTree.setDescription(phylogenyDescription);
		}
		if(phylogenyCreator != null && !phylogenyCreator.isEmpty()) {
			phylogeneticTree.setCreator(phylogenyCreator);
		}
		if(phylogenyRights != null && !phylogenyRights.isEmpty()) {
			phylogeneticTree.setRights(phylogenyRights);
		}
		if(phylogenyLicense != null && !phylogenyLicense.isEmpty()) {
			phylogeneticTree.setLicense(phylogenyLicense);
		}
		if(phylogenyRightsHolder != null && !phylogenyRightsHolder.isEmpty()) {
			phylogeneticTree.setRightsHolder(phylogenyRightsHolder);
		}
		if(rootTaxonIdentifier != null && !rootTaxonIdentifier.isEmpty()) {
			if(rootTaxonIdentifier.indexOf(",") == -1) {
				Taxon rootTaxon = taxonService.find(rootTaxonIdentifier);
				if(rootTaxon != null) {
					phylogeneticTree.getTaxa().add(rootTaxon);
				}
			} else {
				for(String identifier : rootTaxonIdentifier.split(",")) {
					Taxon rootTaxon = taxonService.find(identifier);
					if(rootTaxon != null) {
						phylogeneticTree.getTaxa().add(rootTaxon);
					}
				}
			}

		}

		phylogeneticTree.setPhylogeny(stringBuffer.toString().replaceAll("\n", ""));
		phylogeneticTreeService.saveOrUpdate(phylogeneticTree);
		solrIndexingListener.indexObject(phylogeneticTree);
		logger.info(stringBuffer.toString());
		return RepeatStatus.FINISHED;
	}

	private boolean addBranchLengths(PhylogenyNode node) {
		boolean hasBranchLengths = true;
		if(node.isRoot() || node.getDistanceToParent() != PhylogenyDataUtil.BRANCH_LENGTH_DEFAULT) {
			// do nothing
		} else {
			node.setDistanceToParent(1.0D);
			hasBranchLengths = false;
		}
		for(PhylogenyNode descendant : node.getDescendants()) {
			if(!addBranchLengths(descendant)) {
				hasBranchLengths = false;
			}
		}
		return hasBranchLengths;
	}

	private void addTaxonLinks(PhylogenyNode node, PhylogeneticTree phylogeneticTree) {

		Taxon taxon = matchTaxonName(node.getName());
		if (taxon != null) {
			Annotation annotation = new Annotation();
			annotation.setDesc(taxon.getScientificName());
			List<Uri> uris = new ArrayList<Uri>();
			uris.add(new Uri(baseUri + "/taxon/" + taxon.getIdentifier(), null,	null));
			annotation.setUris(uris);
			node.getNodeData().addAnnotation(annotation);
			if(node.isExternal()) {
				phylogeneticTree.getLeaves().add(taxon);
			}
		}
		for(PhylogenyNode descendant : node.getDescendants()) {
			addTaxonLinks(descendant,phylogeneticTree);
		}
	}

	private Taxon matchTaxonName(String taxonName) {
		if (taxonName == null || taxonName.isEmpty()) {
			return null;
		} else {
			try {
				List<Match<Taxon>> matches = taxonMatcher.match(taxonName);

				Taxon object = null;
				AnnotationType annotationType = null;
				AnnotationCode code = null;
				String text = null;

				if (matches.size() == 0) {
					annotationType = AnnotationType.Error;
					code = AnnotationCode.Absent;
					text = "No matches found for taxonomic name " + taxonName;
				} else if (matches.size() > 1) {
					annotationType = AnnotationType.Error;
					code = AnnotationCode.BadRecord;
					text = matches.size()
							+ " matches found for taxonomic name " + taxonName;
				} else {
					annotationType = AnnotationType.Info;
					code = AnnotationCode.Present;
					object = matches.get(0).getInternal();

					text = object.getIdentifier() + " matches taxonomic name "
							+ taxonName;
				}

				org.emonocot.model.Annotation annotation = new org.emonocot.model.Annotation();
				annotation.setJobId(stepExecution.getJobExecutionId());
				annotation.setAnnotatedObj(object);
				annotation.setRecordType(RecordType.Taxon);
				annotation.setCode(code);
				annotation.setType(annotationType);
				annotation.setValue(taxonName);
				annotation.setText(text);
				super.annotate(annotation);
				return object;
			} catch (UnparsableException e) {
				org.emonocot.model.Annotation annotation = new org.emonocot.model.Annotation();
				annotation.setJobId(stepExecution.getJobExecutionId());
				annotation.setRecordType(RecordType.Taxon);
				annotation.setCode(AnnotationCode.BadField);
				annotation.setType(AnnotationType.Error);
				annotation.setValue(taxonName);
				annotation.setText("Could not parse taxon name " + taxonName);
				super.annotate(annotation);
				return null;
			}
		}
	}

}
