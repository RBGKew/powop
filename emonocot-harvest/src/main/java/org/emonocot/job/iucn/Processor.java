package org.emonocot.job.iucn;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;
import org.emonocot.api.match.Match;
import org.emonocot.api.match.taxon.TaxonMatcher;
import org.emonocot.harvest.common.AbstractRecordAnnotator;
import org.emonocot.model.Annotation;
import org.emonocot.model.MeasurementOrFact;
import org.emonocot.model.Taxon;
import org.emonocot.model.constants.AnnotationCode;
import org.emonocot.model.constants.AnnotationType;
import org.emonocot.model.constants.MeasurementType;
import org.emonocot.model.constants.RecordType;
import org.gbif.ecat.parser.UnparsableException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

/**
 * @author ben
 */
public class Processor extends AbstractRecordAnnotator implements ItemProcessor<Map<String,Object>, MeasurementOrFact> {

	private String accessRights;
	
	private String license;
	
	private String rights;
	
	private String rightsHolder;
	
	private String bibliographicCitation;
	
    /**
	 * @param accessRights the accessRights to set
	 */
	public void setAccessRights(String accessRights) {
		this.accessRights = accessRights;
	}

	/**
	 * @param license the license to set
	 */
	public void setLicense(String license) {
		this.license = license;
	}

	/**
	 * @param rights the rights to set
	 */
	public void setRights(String rights) {
		this.rights = rights;
	}	

	/**
	 * @param bibliographicCitation the bibliographicCitation to set
	 */
	public void setBibliographicCitation(String bibliographicCitation) {
		this.bibliographicCitation = bibliographicCitation;
	}

	/**
	 * @param rightsHolder the rightsHolder to set
	 */
	public void setRightsHolder(String rightsHolder) {
		this.rightsHolder = rightsHolder;
	}

	private Logger logger = LoggerFactory.getLogger(Processor.class);

    private TaxonMatcher taxonMatcher;

    public void setTaxonMatcher(TaxonMatcher taxonMatcher) {
        this.taxonMatcher = taxonMatcher;
    }

    public final MeasurementOrFact process(final Map<String,Object> map) throws Exception {
		Taxon taxon = doMatchTaxon(map);
		if(taxon != null) {
            MeasurementOrFact measurementOrFact = new MeasurementOrFact();
			StringBuffer remarks = new StringBuffer();
			if(map.get("criteria") != null) {
				remarks.append("Criteria: " + map.get("criteria") + ". ");
			}
			if(map.get("modified_year") != null) {
				remarks.append("Modified Year: " + map.get("modified_year") + ". ");
			}
			measurementOrFact.setMeasurementRemarks(remarks.toString().trim());
			measurementOrFact.setMeasurementValue((String)map.get("category"));
			measurementOrFact.setMeasurementType(MeasurementType.valueOf("IUCNConservationStatus"));		
			measurementOrFact.setAccessRights(accessRights);
			measurementOrFact.setRights(rights);
			measurementOrFact.setRightsHolder(rightsHolder);
			measurementOrFact.setLicense(license);
			measurementOrFact.setBibliographicCitation(bibliographicCitation);
			measurementOrFact.setTaxon(taxon);		    
			return measurementOrFact;
		}
        return null;
    }

	private Taxon doMatchTaxon(Map<String, Object> map) {
		String name = (String)map.get("scientific_name");
		if(map.containsKey("authority") && !(((String) map.get("authority")).isEmpty())) {
			String authority = (String) map.get("authority");
			authority = StringEscapeUtils.unescapeXml(authority);
			name = name + " " + authority;
		}
		
		List<Match<Taxon>> results;
		try {
			results = taxonMatcher.match(name);
			if(results.size() == 1) {
				return results.get(0).getInternal();
		    } else if(results.size() > 1) {
				logger.info(name + " multiple matches");
				Annotation annotation = new Annotation();
	            annotation.setJobId(stepExecution.getJobExecutionId());
	            annotation.setAnnotatedObj(null);
	            annotation.setRecordType(RecordType.MeasurementOrFact);
	            annotation.setCode(AnnotationCode.BadRecord);
	            annotation.setType(AnnotationType.Error);
	            annotation.setValue("Species Id: " + (Integer)map.get("species_id"));
	            annotation.setText(results.size() + " matches found for taxonomic name " + name);
	            super.annotate(annotation);
				return null;
			} else {
				logger.info(name + " no matches");
				Annotation annotation = new Annotation();
	            annotation.setJobId(stepExecution.getJobExecutionId());
	            annotation.setAnnotatedObj(null);
	            annotation.setRecordType(RecordType.MeasurementOrFact);
	            annotation.setCode(AnnotationCode.Absent);
	            annotation.setType(AnnotationType.Error);
	            annotation.setValue("Species Id: " + (Integer)map.get("species_id"));
	            annotation.setText("No matches found for taxonomic name " + name);
	            super.annotate(annotation);
				return null;
			}
		} catch (UnparsableException e) {
			logger.info(name + " is unparseable");
			Annotation annotation = new Annotation();
            annotation.setJobId(stepExecution.getJobExecutionId());
            annotation.setAnnotatedObj(null);
            annotation.setRecordType(RecordType.MeasurementOrFact);
            annotation.setCode(AnnotationCode.Absent);
            annotation.setType(AnnotationType.Error);
            annotation.setValue("Species Id: " + (Integer)map.get("species_id"));
            annotation.setText("Taxonomic name " + name + " cannot be parsed");
			return null;
		}
		
	}

}
