package org.emonocot.portal.view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.emonocot.api.job.WCSPTerm;
import org.emonocot.model.Description;
import org.emonocot.model.Distribution;
import org.emonocot.model.MeasurementOrFact;
import org.emonocot.model.Taxon;
import org.emonocot.model.constants.DescriptionType;
import org.emonocot.portal.naturalLanguage.PhraseUtilities;
import org.gbif.dwc.terms.Term;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.i18n.LocaleContextHolder;

public class Summary {

	private Taxon taxon;

	private MessageSource messageSource;

	private PhraseUtilities phraseUtils;

	private static Logger logger = LoggerFactory.getLogger(Summary.class);

	public Summary(Taxon taxon, MessageSource messageSource){
		this.taxon = taxon;
		this.messageSource = messageSource;
		this.phraseUtils = new PhraseUtilities(messageSource);
	}

	public List<String> getMeasurementByType(Term type){
		List<String> measurements = new ArrayList<String>();
		for(MeasurementOrFact item : taxon.getMeasurementsOrFacts()){
			if(item.isType(type)){
				measurements.add(item.getMeasurementValue());
			}
		}
		return measurements;
	}



	private String getDistribution(){
		Map<String, List<String>> distributionsByContinent = new HashMap<String, List<String>>();
		List<String> distributionList = new ArrayList<String>();
		if(!taxon.getDistribution().isEmpty()){
			for(Distribution distribution : taxon.getDistribution()){
				if(distribution.getEstablishmentMeans() == null){
					String key = distribution.getLocation().getContinent().toString();
					if(distributionsByContinent.containsKey(key)){
						List<String> locations = distributionsByContinent.get(key);
						locations.add(distribution.getLocation().toString());
						distributionsByContinent.put(key, locations);
					}else{
						List<String> locations = new ArrayList<String>();
						locations.add(distribution.getLocation().toString());
						distributionsByContinent.put(key, locations);
					}
				}
			}
		}
		distributionsByContinent = phraseUtils.shortenToSuperType(distributionsByContinent);
		for(List<String> list : distributionsByContinent.values()){
			distributionList.addAll(list);
		}
		return phraseUtils.constructList(distributionList);
	}

	private String buildLocationandHabitat(){
		String distribution = getDistribution();
		String rank ="";
		if(taxon.getTaxonRemarks() != null){
			rank = taxon.getTaxonRank().toString().toLowerCase();
		}else{
			rank = "plant";
		}
		if(!distribution.isEmpty()){
			return String.format("This %s is accepted, and is native to %s.", rank, distribution);
		}
		return null;
	}



	public String build(){
		if(taxon.isAccepted()){
			String location =  buildLocationandHabitat();
			String uses = new SummaryUses().buildUses(taxon, messageSource);

			if(location != null && uses != null && !uses.isEmpty()) {
				return String.format("%s It is %s.", location, uses);
			} else if(location != null) {
				return location;
			} else if(uses != null && !uses.isEmpty()) {
				if(taxon.getTaxonRank() != null) {
					return String.format("This %s is accepted, and is %s.", taxon.getTaxonRank().toString().toLowerCase(), uses);
				} else {
					return String.format("This plant is %s.", uses);
				}
			} else {
				String thing = taxon.getTaxonRank() == null ? "plant" : taxon.getTaxonRank().toString().toLowerCase();
				return String.format("This %s is accepted.", thing);
			}
		}
		if(taxon.getTaxonRank() != null) {
			return String.format("This %s is a synonym of ", taxon.getTaxonRank().toString().toLowerCase());
		}
		return String.format("This is a synonym of ", taxon.getTaxonRank().toString().toLowerCase());
	}
}
