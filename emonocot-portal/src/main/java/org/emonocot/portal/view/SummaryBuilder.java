package org.emonocot.portal.view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.emonocot.model.Description;
import org.emonocot.model.MeasurementOrFact;
import org.emonocot.model.constants.DescriptionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.i18n.LocaleContextHolder;

public class SummaryBuilder {

	private String distribution;
	
	private String lifeform;
	
	private String rank;
	
	private String habitat;
	
	private Set<Description> uses;
	
	private MessageSource messageSource;
	
	private static Logger logger = LoggerFactory.getLogger(SummaryBuilder.class);
	
	public SummaryBuilder taxonRemarks(String taxonRemarks){
		this.distribution = translate(taxonRemarks);
		return this;
	}
	
	public SummaryBuilder lifeform(String string){
		this.lifeform = translate(string);
		return this;
	}
	
	public SummaryBuilder rank (String rank){
		this.rank = translate(rank);
		return this;
	}
	
	public SummaryBuilder habitat(MeasurementOrFact habitat){
		if(habitat != null){
		this.habitat = translate(habitat.getMeasurementValue());
		}
		return this;
	}
	
	public SummaryBuilder uses(Set<Description> uses){
		this.uses = uses;
		return this;
	}
	
	public SummaryBuilder messageSource(MessageSource messageSource){
		this.messageSource = messageSource;
		return this;
	}
	
	protected Locale currentLocale() {
		    return LocaleContextHolder.getLocale();
	}
	
	private String translate(String word){
		if(word != null && !word.isEmpty()){
			return messageSource.getMessage(word, new Object[] {}, word, Locale.getDefault()).toLowerCase();
		}
		return null;
	}
	
	private Map<DescriptionType, List<DescriptionType>> getUsesBySuperType(){
		DescriptionType[] supertypes = {DescriptionType.usePoisons, DescriptionType.useAnimalFood, DescriptionType.useFood,
				DescriptionType.useFuel, DescriptionType.useMedicines, DescriptionType.useEnvironmentalUse,
				DescriptionType.useSocialUse, DescriptionType.useWeedImpact};

		Map<DescriptionType, List<DescriptionType>> descriptionsBySuperType = new HashMap<DescriptionType,List<DescriptionType>>();
		for(Description use : uses){
			for(DescriptionType type : use.getTypes()) {
				for(DescriptionType supertype : supertypes){
					List<DescriptionType> descriptions = new ArrayList<DescriptionType>();
					if(type.isA(supertype)){
						if(descriptionsBySuperType.containsKey(supertype)){
							descriptions = descriptionsBySuperType.get(supertype);	
						}
						descriptions.add(use.getType());
						descriptionsBySuperType.put(supertype, descriptions);
					}
				}
			}
		}
		return descriptionsBySuperType;
	}
	
	private String constructList(Collection<String> collection){
		List<String> list = new ArrayList<String>(collection);
		StringBuilder phrase = new StringBuilder();
		int i = 0;
		while(i < list.size()){
			phrase.append(translate(list.get(i)));
			i++;
			if(i + 1 < list.size()){
				phrase.append(", ");
			}else if(i + 1 == list.size()){
				phrase.append(" and ");
			}
		}
		return phrase.toString();
		
	}
	
	private String buildLocationandHabitat(){
		if(distribution != null && habitat != null){
			return String.format("Found in %s, this %s occurs in %s.", habitat, lifeform, distribution);
		}else if(distribution != null){
			return String.format("This %s occurs in %s.", lifeform, distribution);
		}else if(habitat != null){
			return String.format("This %s occurs in %s.", lifeform, habitat);
		}
		return null;
	}
	
	private String buildUses(String firstPhrase){
		if(uses != null && !uses.isEmpty()){
			Map<DescriptionType, List<DescriptionType>> descriptionsBySuperType = getUsesBySuperType();
				if(!descriptionsBySuperType.isEmpty()){
					List<String> verbHas = new ArrayList<String>();
					List<String> verbAs = new ArrayList<String>();
					List<String> verbFor = new ArrayList<String>();
					String phraseUses = " It ";
					if(firstPhrase == null){
						phraseUses = String.format(" This %s ", lifeform);
					}
					for(DescriptionType key : descriptionsBySuperType.keySet()){
						if(key.isA(DescriptionType.useSocialUse) || key.isA(DescriptionType.useEnvironmentalUse)){
							verbHas.add(key.toString());
						}else if(key.isA(DescriptionType.usePoisons)){
							verbAs.add(key.toString());
						}else{
							verbFor.add(key.toString());
						}
					}
					if(!verbFor.isEmpty()){
						phraseUses += " is used for " + constructList(verbFor) + ".";
						if(!verbHas.isEmpty()){
							phraseUses += " This " + rank + " also ";
						}
					}
					if(!verbHas.isEmpty()){
						phraseUses +=  " has " + constructList(verbHas) + " uses";
						if(!verbAs.isEmpty()){
							phraseUses += " and ";
						}else{
							phraseUses += ".";
						}
					}
					if(!verbAs.isEmpty()){
						phraseUses +=  " is used as a " + constructList(verbAs) + ".";
					}
				return firstPhrase + phraseUses;
			}
		}
		return firstPhrase;	
	
	}
	
	public String build(){
		if(lifeform == null){
			if(rank != null){
				this.lifeform = rank;
				this.rank = "plant";
			}else{
				this.rank = "plant";
				this.lifeform = "plant";
			}
		}
		return buildUses(buildLocationandHabitat());
	}
}
