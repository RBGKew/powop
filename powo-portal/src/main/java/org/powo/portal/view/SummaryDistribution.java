package org.powo.portal.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import org.powo.model.Distribution;
import org.powo.model.Taxon;
import org.powo.model.solr.QueryOption;
import org.powo.portal.naturalLanguage.PhraseUtilities;
import org.springframework.context.MessageSource;

import com.google.common.collect.ImmutableMap;

public class SummaryDistribution {

	
	///notes: replace ? & (?) with " and its native range is likely to be x // its native range is not known, but is thought to be x	
	private static final Map<Pattern, String> transformerDoubtful = new ImmutableMap.Builder<Pattern, String>()
			.put(Pattern.compile("\\(\\?\\)"), "")
			.put(Pattern.compile("\\?"), "")
			.build();

	private static final Map<Pattern, String> transformerAbreviations = new ImmutableMap.Builder<Pattern, String>()
			.put(Pattern.compile("I\\."), "Island")
			.put(Pattern.compile("Is\\."), "Islands")
			.put(Pattern.compile("Trop\\."), "Tropical")
			.put(Pattern.compile("Arch\\."), "Archipelago")
			.put(Pattern.compile("Co\\."), "County")
			.put(Pattern.compile("Distr\\."), "District")
			.put(Pattern.compile("Pen\\."), "Peninsula")
			.put(Pattern.compile("Subtrop\\."), "Subtropical")
			.put(Pattern.compile("Rep\\."), "Republic")
			.put(Pattern.compile("Mts\\."), "Mountains")
			.put(Pattern.compile("NC\\."), "N. Central")
			.put(Pattern.compile("SC\\."), "S. Central")
			.put(Pattern.compile("WC\\."), "W. Central")
			.put(Pattern.compile("EC\\."), "E. Central")
			.put(Pattern.compile("\\sC\\."), " Central")
			.put(Pattern.compile("^C\\."), "Central")
			//strip trailing punctuation
			.put(Pattern.compile("^[^a-zA-Z]+"), "")
			.build();
	
	private Taxon taxon;
	
	
	private PhraseUtilities phraseUtils;

	public SummaryDistribution(Taxon taxon, MessageSource messageSource){
		this.taxon = taxon;
		this.phraseUtils = new PhraseUtilities(messageSource);
	}
	
	private String wcsDistribution(){
    	String distribution = taxon.getTaxonRemarks().trim();
    	boolean doubtful = false;
    	for(Entry<Pattern, String> transformer : transformerAbreviations.entrySet()){
    		distribution = transformer.getKey().matcher(distribution).replaceAll(transformer.getValue());
    	}
    	for(Entry<Pattern, String> transformer : transformerDoubtful.entrySet()){
    		if(transformer.getKey().matcher(distribution).matches()){
    			doubtful = true;
    			distribution = transformer.getKey().matcher(distribution).replaceAll(transformer.getValue());
    		}
    	}
		if(doubtful){
			return String.format("its native range is likely to be %s", distribution);
		}
		return String.format("its native range is %s", distribution);
	}
	
	private String tdwgDistribution(){
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
		String distribution = phraseUtils.constructList(distributionList);
		if(distribution != null && !distribution.isEmpty()){
			return String.format("is native to %s.", distribution);
		}
		return null;
	}
	
	public String build(){
	    if(taxon.getTaxonRemarks() != null && !taxon.getTaxonRemarks().isEmpty()){
	    	return wcsDistribution();
	    }
	    return tdwgDistribution();
	}
	
}