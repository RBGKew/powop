package org.emonocot.job.iucn;

import java.util.Map;

import org.emonocot.api.TaxonService;
import org.emonocot.model.MeasurementOrFact;
import org.emonocot.model.Taxon;
import org.emonocot.model.constants.MeasurementType;
import org.emonocot.pager.Page;
import org.gbif.ecat.voc.Rank;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

/**
 * @author ben
 */
public class Processor implements ItemProcessor<Map<String,Object>, MeasurementOrFact> {

    /**
		*
     */
    private Logger logger = LoggerFactory.getLogger(Processor.class);

    /**
     *
     */
    private TaxonService taxonService;

    /**
     * @param newTaxonService
     *            the taxon service to set
     */
    public void setTaxonService(TaxonService taxonService) {
        this.taxonService = taxonService;
    }

    /**
     * @param taxon
     *            Set the taxon
     * @return a result
     * @throws Exception
     *             if there is a problem
     */
    public final MeasurementOrFact process(final Map<String,Object> map) throws Exception {
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
			MeasurementOrFact measurementOrFact = new MeasurementOrFact();
			measurementOrFact.setIdentifier(((Integer)map.get("species_id")).toString());
			measurementOrFact.setMeasurementRemarks((String)map.get("criteria"));
			DateTime dateTime = new DateTime();
			//measurementOrFact.setMeasurementDeterminedDate();
			measurementOrFact.setMeasurementValue((String)map.get("category"));
			measurementOrFact.setMeasurementType(MeasurementType.valueOf("IUCNConservationStatus"));
			
		    Taxon taxon = results.getRecords().get(0);
		    if(taxon.getAcceptedNameUsage() == null) {
				measurementOrFact.setTaxon(taxon);
		        logger.debug(taxon.getScientificName() + " " + map.get("category"));
		    } else {
				measurementOrFact.setTaxon(taxon.getAcceptedNameUsage());
				logger.debug(taxon.getAcceptedNameUsage().getScientificName() + " (as synonym " + taxon.getScientificName() + ") " + map.get("category"));
			}
			return measurementOrFact;
	    } else if(results.getSize() > 1) {
			logger.error(map.get("scientific_name")  + " " + authority + " multiple matches");
		} else {
			logger.error(map.get("scientific_name") + " " + authority + " no matches");
		}
        return null;
    }

}
