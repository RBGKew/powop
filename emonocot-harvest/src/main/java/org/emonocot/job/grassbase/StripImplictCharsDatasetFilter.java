/*
 * This is eMonocot, a global online biodiversity information resource.
 *
 * Copyright © 2011–2015 The Board of Trustees of the Royal Botanic Gardens, Kew and The University of Oxford
 *
 * eMonocot is free software: you can redistribute it and/or modify it under the terms of the
 * GNU Affero General Public License as published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * eMonocot is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * The complete text of the GNU Affero General Public License is in the source repository as the file
 * ‘COPYING’.  It is also available from <http://www.gnu.org/licenses/>.
 */
package org.emonocot.job.grassbase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.ala.delta.DeltaContext;
import au.org.ala.delta.model.Character;
import au.org.ala.delta.model.Item;
import au.org.ala.delta.model.MultiStateAttribute;
import au.org.ala.delta.model.MultiStateCharacter;
import au.org.ala.delta.translation.naturallanguage.NaturalLanguageDataSetFilter;

/**
 * Based on STRIPIMPLICIT vb6 script written by Kehan Harman
 * @author ben
 *
 */
public class StripImplictCharsDatasetFilter extends	NaturalLanguageDataSetFilter {

	Logger logger = LoggerFactory.getLogger(StripImplictCharsDatasetFilter.class);

	public StripImplictCharsDatasetFilter(DeltaContext context) {
		super(context);
	}

	@Override
	public boolean filter(Item item, Character character) {

		if(character instanceof MultiStateCharacter) {
			MultiStateCharacter multiStateCharacter = (MultiStateCharacter) character;
			MultiStateAttribute attribute = (MultiStateAttribute)item.getAttribute(character);

			logger.debug("Character " + character.getCharacterId() + " being considered for stripping. Implicit State " + multiStateCharacter.getUncodedImplicitState() + " actual value " + attribute.getValueAsString());

			if(multiStateCharacter.getUncodedImplicitState() > 0 && attribute.getPresentStates().size() == 1 && (multiStateCharacter.getUncodedImplicitState() == attribute.getFirstStateCoded())) {
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
