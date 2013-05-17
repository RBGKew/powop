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
import org.forester.io.parsers.PhylogenyParser;
import org.forester.io.parsers.util.ParserUtils;
import org.forester.io.writers.PhylogenyWriter;
import org.forester.phylogeny.Phylogeny;
import org.forester.phylogeny.PhylogenyMethods;
import org.forester.phylogeny.PhylogenyNode;
import org.forester.phylogeny.data.Annotation;
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
	
	private PhylogeneticTreeService phylogeneticTreeService;
	
	private TaxonService taxonService;

	public void setTreeIdentifier(String treeIdentifier) {
		this.treeIdentifier = treeIdentifier;
	}
	
	public void setRootTaxonIdentifier(String rootTaxonIdentifier) {
		this.rootTaxonIdentifier = rootTaxonIdentifier;
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
		
		addTaxonLinks(node);
		StringBuffer stringBuffer = phylogenyWriter.toPhyloXML(phylogeny, 1);
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
		}
		phylogeneticTree.setNumberOfExternalNodes(new Long(phylogeny.getNumberOfExternalNodes()));
		
		if(phylogeneticTree.getTitle() == null || phylogeneticTree.getTitle().isEmpty()) {
		    phylogeneticTree.setTitle(phylogeny.getName());
		}
		if(phylogeneticTree.getDescription() == null || phylogeneticTree.getDescription().isEmpty()) {
		    phylogeneticTree.setDescription(phylogeny.getDescription());
		}
		if(phylogenyTitle != null && !phylogenyTitle.isEmpty()) {
			phylogeneticTree.setTitle(phylogenyTitle);
		}
		
		if(rootTaxonIdentifier != null && !rootTaxonIdentifier.isEmpty()) {
			Taxon rootTaxon = taxonService.find(rootTaxonIdentifier);
			if(rootTaxon != null) {
				phylogeneticTree.getTaxa().add(rootTaxon);
			}
		}
		
		phylogeneticTree.setPhylogeny(stringBuffer.toString().replaceAll("\n", ""));
		phylogeneticTreeService.saveOrUpdate(phylogeneticTree);
        logger.info(stringBuffer.toString());
		return RepeatStatus.FINISHED;
	}
	
	private void addTaxonLinks(PhylogenyNode node) {
		Taxon taxon = matchTaxonName(node.getName());
		if (taxon != null) {
			Annotation annotation = new Annotation();
			annotation.setDesc(taxon.getScientificName());
			List<Uri> uris = new ArrayList<Uri>();
			uris.add(new Uri(baseUri + "/taxon/" + taxon.getIdentifier(), null,	null));
			annotation.setUris(uris);
			node.getNodeData().addAnnotation(annotation);
			
		}
		for(PhylogenyNode descendant : node.getDescendants()) {
			addTaxonLinks(descendant);
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
