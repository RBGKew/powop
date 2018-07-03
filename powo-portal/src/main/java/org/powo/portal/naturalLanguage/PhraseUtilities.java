package org.powo.portal.naturalLanguage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.text.WordUtils;
import org.powo.model.constants.DescriptionType;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

public class PhraseUtilities {
	
	private MessageSource messageSource;
	
	protected Locale currentLocale() {
	    return LocaleContextHolder.getLocale();
	}
	
	public PhraseUtilities(MessageSource messageSource){
		this.messageSource = messageSource;
	}
	
	public String translate(String word){
		if(word != null && !word.isEmpty()){
			return messageSource.getMessage(word, new Object[] {}, word, Locale.getDefault());
		}
		return null;
	}

	public String constructList(Collection<String> collection){
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
	
	public Map<String, List<String>> shortenToSuperType(Map<String, List<String>> mapBySuperType){
		Integer listLength = 0; 
		for(List<String> list : mapBySuperType.values()){
			listLength += list.size();
		}
		for(Entry<String, List<String>> superType :  mapBySuperType.entrySet()){
			if(superType.getValue().size() > 3 || (superType.getValue().size() != 1 && listLength >= 9)){
				mapBySuperType.put(superType.getKey(), Arrays.asList(superType.getKey()));
			}
		}
		return mapBySuperType;
	}
	
	public Map<DescriptionType, List<DescriptionType>> shortenDescToSuperType(Map<DescriptionType, List<DescriptionType>> mapBySuperType){
		Integer listLength = 0; 
		for(List<DescriptionType> list : mapBySuperType.values()){
			listLength += list.size();
		}
		for(Entry<DescriptionType, List<DescriptionType>> superType :  mapBySuperType.entrySet()){
			if(superType.getValue().size() > 3 || (superType.getValue().size() != 1 && listLength >= 9)){
				mapBySuperType.put(superType.getKey(), Arrays.asList(superType.getKey()));
			}
		}
		return mapBySuperType;
	}
	
	public String cleanSentence(String string){
		// strips double full stops, adds trailing full stop, capitalizes
		string += ".";
		return string.replace("..", ".");
	}
	
}
