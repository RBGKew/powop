package org.emonocot.job.iucn;

import java.util.Map;

import org.emonocot.api.TaxonService;
import org.emonocot.harvest.common.AbstractRecordAnnotator;
import org.emonocot.model.Annotation;
import org.emonocot.model.MeasurementOrFact;
import org.emonocot.model.Taxon;
import org.emonocot.model.constants.AnnotationCode;
import org.emonocot.model.constants.AnnotationType;
import org.emonocot.model.constants.MeasurementType;
import org.emonocot.model.constants.RecordType;
import org.emonocot.pager.Page;
import org.gbif.ecat.voc.Rank;
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

    private TaxonService taxonService;

    public void setTaxonService(TaxonService taxonService) {
        this.taxonService = taxonService;
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
		Taxon example = new Taxon();
		example.setGenus((String)map.get("genus"));
		example.setSpecificEpithet((String)map.get("species"));
		String infraSpecificEpithet = (String)map.get("infra_name");
		String authority = null;
		if(infraSpecificEpithet != null) {
			example.setInfraspecificEpithet(infraSpecificEpithet);
			authority = (String)map.get("infra_authority");
		} else {
			authority = (String)map.get("authority");
			example.setTaxonRank(Rank.SPECIES);
		}
		if(authority != null) {
		    authority = authority.replaceAll("&amp;","&");
		    if(authority.indexOf(" ") != -1) {
		        StringBuffer stringBuffer = new StringBuffer();
		        String[] subs = authority.split(" ");
		        stringBuffer.append(subs[0]);
		        for(int i = 1; i < subs.length; i++) {
					String prev = subs[i -1];
					String next = subs[i];
					if(prev.length() > 1 && 
					   prev.charAt(prev.length() - 1) == '.' 
					   && next.charAt(0) >= 'A' 
					   && next.charAt(0) <= 'Z') {
						stringBuffer.append(next);
					} else {
						stringBuffer.append(' ');
						stringBuffer.append(next);
					} 
				}
				authority = stringBuffer.toString();
		    }
		    authority = authority.trim();
	    }
	    example.setScientificNameAuthorship(authority);
		Page<Taxon> results = taxonService.searchByExample(example, true, true);
		if(results.getSize() == 1) {
			return results.getRecords().get(0);
	    } else if(results.getSize() > 1) {
			logger.info(map.get("scientific_name")  + " " + authority + " multiple matches");
			Annotation annotation = new Annotation();
            annotation.setJobId(stepExecution.getJobExecutionId());
            annotation.setAnnotatedObj(null);
            annotation.setRecordType(RecordType.MeasurementOrFact);
            annotation.setCode(AnnotationCode.BadRecord);
            annotation.setType(AnnotationType.Error);
            annotation.setValue("Species Id: " + (Integer)map.get("species_id"));
            annotation.setText(results.getSize() + " matches found for taxonomic name " + map.get("scientific_name")  + " " + authority);
            super.annotate(annotation);
			return null;
		} else {
			logger.info(map.get("scientific_name") + " " + authority + " no matches");
			Annotation annotation = new Annotation();
            annotation.setJobId(stepExecution.getJobExecutionId());
            annotation.setAnnotatedObj(null);
            annotation.setRecordType(RecordType.MeasurementOrFact);
            annotation.setCode(AnnotationCode.Absent);
            annotation.setType(AnnotationType.Error);
            annotation.setValue("Species Id: " + (Integer)map.get("species_id"));
            annotation.setText("No matches found for taxonomic name " + map.get("scientific_name")  + " " + authority);
            super.annotate(annotation);
			return null;
		}
	}

}
