package org.emonocot.job.gbif;

import java.util.List;

import org.emonocot.api.match.Match;
import org.emonocot.api.match.taxon.TaxonMatcher;
import org.emonocot.harvest.common.AbstractRecordAnnotator;
import org.emonocot.model.Annotation;
import org.emonocot.model.Taxon;
import org.emonocot.model.TypeAndSpecimen;
import org.emonocot.model.constants.AnnotationCode;
import org.emonocot.model.constants.AnnotationType;
import org.emonocot.model.constants.RecordType;
import org.gbif.ecat.model.ParsedName;
import org.gbif.ecat.parser.NameParser;
import org.gbif.ecat.parser.UnparsableException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

public class Processor extends AbstractRecordAnnotator implements ItemProcessor<TaxonOccurrence,TypeAndSpecimen> {
	
	private Logger logger = LoggerFactory.getLogger(Processor.class);

    private TaxonMatcher taxonMatcher;
    
    private NameParser nameParser;

    public void setTaxonMatcher(TaxonMatcher taxonMatcher) {
        this.taxonMatcher = taxonMatcher;
    }
	
	public TypeAndSpecimen process(TaxonOccurrence o) throws Exception {
		Taxon taxon = doMatchTaxon(o);
		if(taxon != null) {
            TypeAndSpecimen typeAndSpecimen = new TypeAndSpecimen();
			typeAndSpecimen.setIdentifier(o.getAbout());
			typeAndSpecimen.setSource(o.getAbout());
			
			typeAndSpecimen.setRights(o.getRights());
			typeAndSpecimen.setRightsHolder(o.getRightsHolder());
			typeAndSpecimen.setLicense(o.getLicense());
			typeAndSpecimen.setBibliographicCitation(o.getBibliographicCitation());
			typeAndSpecimen.getTaxa().add(taxon);
			typeAndSpecimen.setScientificName(o.getIdentifiedTo().get(0).getTaxonName());
			typeAndSpecimen.setCatalogNumber(o.getCatalogNumber());
			typeAndSpecimen.setCollectionCode(o.getCollectionCode());
			typeAndSpecimen.setInstitutionCode(o.getInstitutionCode());
			typeAndSpecimen.setLocality(o.getLocality());
			typeAndSpecimen.setDecimalLatitude(o.getDecimalLatitude());
			typeAndSpecimen.setDecimalLongitude(o.getDecimalLongitude());
			return typeAndSpecimen;
		}
        return null;
	}

	private Taxon doMatchTaxon(TaxonOccurrence o) throws UnparsableException {
		if(o.getIdentifiedTo() == null || o.getIdentifiedTo().isEmpty() || o.getIdentifiedTo().get(0).getTaxonName() == null) {
			return null;
		} else {
			ParsedName<String> parsedName = nameParser.parse(o.getIdentifiedTo().get(0).getTaxonName());
			List<Match<Taxon>> results = taxonMatcher.match(parsedName);
			
			if(results.size() == 1) {
				return results.get(0).getInternal();
		    } else if(results.size() > 1) {
				logger.info(o.getIdentifiedTo().get(0).getTaxonName()  + " multiple matches");
				Annotation annotation = new Annotation();
	            annotation.setJobId(stepExecution.getJobExecutionId());
	            annotation.setAnnotatedObj(null);
	            annotation.setRecordType(RecordType.TypeAndSpecimen);
	            annotation.setCode(AnnotationCode.BadRecord);
	            annotation.setType(AnnotationType.Error);
	            annotation.setValue("GBIF Record : " + o.getAbout());
	            annotation.setText(results.size() + " matches found for taxonomic name " + o.getIdentifiedTo().get(0).getTaxonName());
	            super.annotate(annotation);
				return null;
			} else {
				logger.info(o.getIdentifiedTo().get(0).getTaxonName() + " no matches");
				Annotation annotation = new Annotation();
	            annotation.setJobId(stepExecution.getJobExecutionId());
	            annotation.setAnnotatedObj(null);
	            annotation.setRecordType(RecordType.TypeAndSpecimen);
	            annotation.setCode(AnnotationCode.Absent);
	            annotation.setType(AnnotationType.Error);
	            annotation.setValue("GBIF Record : " + o.getAbout());
	            annotation.setText("No matches found for taxonomic name " + o.getIdentifiedTo().get(0).getTaxonName());
	            super.annotate(annotation);
				return null;
			}
		}
	}

	public void setNameParser(NameParser nameParser) {
		this.nameParser = nameParser;
	}
}
