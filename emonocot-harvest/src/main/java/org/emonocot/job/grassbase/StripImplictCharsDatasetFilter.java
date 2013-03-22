package org.emonocot.job.grassbase;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.ala.delta.DeltaContext;
import au.org.ala.delta.model.Character;
import au.org.ala.delta.model.Item;
import au.org.ala.delta.model.MultiStateAttribute;
import au.org.ala.delta.translation.naturallanguage.NaturalLanguageDataSetFilter;

/**
 * Based on STRIPIMPLICIT vb6 script written by Kehan Harman
 * @author ben
 *
 */
public class StripImplictCharsDatasetFilter extends
		NaturalLanguageDataSetFilter {
	
	Logger logger = LoggerFactory.getLogger(StripImplictCharsDatasetFilter.class);
	
	
	Map<Integer,Integer> charStatesToStrip = new HashMap<Integer,Integer>();
	
	public void setCharStatesToStrip(Map<Integer,Integer> charStatesToStrip) {
		this.charStatesToStrip = charStatesToStrip;
	}

	public StripImplictCharsDatasetFilter(DeltaContext context) {
		super(context);
	}

	@Override
	public boolean filter(Item item, Character character) {
		
		if(charStatesToStrip.containsKey(character.getCharacterId())) {
			MultiStateAttribute attribute = (MultiStateAttribute)item.getAttribute(character);
			logger.debug("Character " + character.getCharacterId() + " being considered for stripping. Implicit State " + charStatesToStrip.get(character.getCharacterId()) + " actual value " + attribute.getValueAsString());
			
			if(attribute.getPresentStates().size() == 1 && charStatesToStrip.get(character.getCharacterId()).equals(attribute.getFirstStateCoded())) {
				logger.debug("filtering " + character.getCharacterId());
				return false;
			} else {
				return super.filter(item, character);
			}
		} else {
    		return super.filter(item, character);
		}
	}
	
	

}
