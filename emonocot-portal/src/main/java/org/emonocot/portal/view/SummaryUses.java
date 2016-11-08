package org.emonocot.portal.view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.emonocot.model.Description;
import org.emonocot.model.Taxon;
import org.emonocot.model.constants.DescriptionType;
import org.emonocot.portal.naturalLanguage.CsvResourceLoader;
import org.emonocot.portal.naturalLanguage.PhraseUtilities;
import org.emonocot.portal.view.Descriptions.DescriptionsBySource;
import org.emonocot.portal.view.Descriptions.DescriptionsByType;
import org.springframework.context.MessageSource;

public class SummaryUses {

	private EnumMap<DescriptionType, String[]> descriptionTranslations = new CsvResourceLoader().loadDescriptions("uses.csv");
	
	private Map<DescriptionType, List<DescriptionType>> descriptionsBySuperType = new HashMap<DescriptionType,List<DescriptionType>>();
	
	private PhraseUtilities phraseUtils;
	
	
	
	private DescriptionType[] supertypes = {DescriptionType.usePoisons, DescriptionType.useAnimalFood, DescriptionType.useFood,
			DescriptionType.useFuel, DescriptionType.useMedicines, DescriptionType.useEnvironmentalUse,
			DescriptionType.useSocialUse, DescriptionType.useWeedImpact};
	
	public String buildUses(Taxon taxon, MessageSource messageSource){
		this.phraseUtils = new PhraseUtilities( messageSource);
		Set<Taxon> taxonAndSynonyms = taxon.getSynonymNameUsages();
		taxonAndSynonyms.add(taxon);
		for(Taxon item : taxonAndSynonyms){
			for(Description description : item.getDescriptions()){
				for(DescriptionType type : description.getTypes()){
					for(DescriptionType supertype : supertypes){
						if(type.isA(supertype)){
							if(descriptionsBySuperType.containsKey(supertype)){
								List<DescriptionType> descriptionsByType = descriptionsBySuperType.get(supertype);
								descriptionsByType.add(type);
								descriptionsBySuperType.put(supertype, descriptionsByType);
							}else{
								List<DescriptionType> descriptionsByType = new ArrayList<DescriptionType>();
								descriptionsByType.add(type);
								descriptionsBySuperType.put(supertype, descriptionsByType);
							}
						}
					}
				}
			}
		}	

		return usesByPreposition();
	}
	
	private String usesByPreposition(){
		descriptionsBySuperType = phraseUtils.shortenDescToSuperType(descriptionsBySuperType);
		Map<String, Set<String>> usesByPreposition = new HashMap<String, Set<String>>();
		List<String> phraseList = new ArrayList<String>();
		for(List<DescriptionType> list : descriptionsBySuperType.values()){
			for(DescriptionType type : list){
				String[] fragment = descriptionTranslations.get(type);
				if(usesByPreposition.containsKey(fragment[2])){
					Set<String> sentenceObjects = usesByPreposition.get(fragment[2]);
					sentenceObjects.add(fragment[3].toLowerCase());
					usesByPreposition.put(fragment[2], sentenceObjects);
				}else{
					Set<String> sentenceObjects = new HashSet<String>();
					sentenceObjects.add(fragment[3].toLowerCase());
					usesByPreposition.put(fragment[2], sentenceObjects);
				}
			}
		}
		for(Entry<String, Set<String>> entrySet : usesByPreposition.entrySet()){
			phraseList.add(String.format(entrySet.getKey(), phraseUtils.constructList(entrySet.getValue())));
		}
		return phraseUtils.constructList(phraseList);
	}
	
}
