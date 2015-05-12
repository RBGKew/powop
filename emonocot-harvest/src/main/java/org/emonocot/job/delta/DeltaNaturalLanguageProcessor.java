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
package org.emonocot.job.delta;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.emonocot.api.match.Match;
import org.emonocot.api.match.taxon.TaxonMatcher;
import org.emonocot.model.Description;
import org.emonocot.model.Taxon;
import org.gbif.ecat.parser.UnparsableException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import au.org.ala.delta.DeltaContext;
import au.org.ala.delta.model.Attribute;
import au.org.ala.delta.model.Character;
import au.org.ala.delta.model.Item;
import au.org.ala.delta.model.MutableDeltaDataSet;
import au.org.ala.delta.model.format.AttributeFormatter;
import au.org.ala.delta.model.format.CharacterFormatter;
import au.org.ala.delta.model.format.ItemFormatter;
import au.org.ala.delta.translation.DataSetFilter;
import au.org.ala.delta.translation.FormatterFactory;
import au.org.ala.delta.translation.ItemListTypeSetter;
import au.org.ala.delta.translation.IterativeTranslator;
import au.org.ala.delta.translation.PrintFile;
import au.org.ala.delta.translation.TypeSetterFactory;
import au.org.ala.delta.translation.naturallanguage.NaturalLanguageTranslator;

public class DeltaNaturalLanguageProcessor implements ItemProcessor<Item,Description> {
	
	private Logger logger = LoggerFactory.getLogger(DeltaNaturalLanguageProcessor.class);
	
	private DeltaContext deltaContext;
	
	private DataSetFilter filter;
	
	private TaxonMatcher taxonMatcher;	
	
	private Integer characterForLink;
	
	private String linkPrefix;
	
	private String linkSuffix;
	
	public void setCharacterForLink(Integer characterForLink) {
		this.characterForLink = characterForLink; 
	}
	
	public void setLinkPrefix(String linkPrefix) {
		this.linkPrefix = linkPrefix;
	}
	
	public void setLinkSuffix(String linkSuffix) {
		this.linkSuffix = linkSuffix;
	}
	
	
 	public void setDeltaContextHolder(DeltaContextHolder deltaContextHolder) {
		assert deltaContextHolder != null;
		this.deltaContext = deltaContextHolder.getDeltaContext();
	}
	
	public void setFilter(DataSetFilter filter) {
		this.filter = filter;
	}

	public void setTaxonMatcher(TaxonMatcher taxonMatcher) {
		this.taxonMatcher = taxonMatcher;
	}
	
	private String translate(Item item) throws UnsupportedEncodingException {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		PrintStream printStream = new PrintStream(byteArrayOutputStream);
		PrintFile printFile = new PrintFile(printStream,0);
        printFile.setTrimInput(false);       
        
		FormatterFactory formatterFactory = new FormatterFactory(deltaContext);
        ItemListTypeSetter typeSetter = new TypeSetterFactory().createTypeSetter(deltaContext, printFile);
        ItemFormatter itemFormatter  = formatterFactory.createItemFormatter(typeSetter);
		CharacterFormatter characterFormatter = formatterFactory.createCharacterFormatter();
		AttributeFormatter attributeFormatter = formatterFactory.createAttributeFormatter();
       
        IterativeTranslator translator = new NaturalLanguageTranslator(deltaContext, typeSetter, printFile, itemFormatter, characterFormatter, attributeFormatter);
	    
		MutableDeltaDataSet dataSet = deltaContext.getDataSet();
					
		int numChars = dataSet.getNumberOfCharacters();
		for (int i=1; i<=numChars; i++) {
				
			au.org.ala.delta.model.Character character = dataSet.getCharacter(i);			
			Attribute attribute = item.getAttribute(character);

			if (filter.filter(item, character)) {
				logger.info(item.getItemNumber() + ", "+character.getCharacterId()+" = "+attribute.getValueAsString());
				translator.beforeAttribute(attribute);
				logger.info("translating " + character + " : " + attribute.getValueAsString());
				translator.afterAttribute(attribute);
			} 
		}
			
		translator.afterItem(item);
		translator.afterLastItem();
		
		printFile.close();
		printStream.close();
		
		String output = byteArrayOutputStream.toString("UTF-8");
		logger.debug(output.trim());
		return output.trim();
	}
	
	private Taxon getTaxon(String scientificName) throws UnparsableException {
		List<Match<Taxon>> matches = taxonMatcher.match(scientificName);
		if(matches.isEmpty()) {
			logger.warn("No matches for " + scientificName);
			return null;
		} else if(matches.size() == 1) {
			Match<Taxon> match = matches.get(0);
			
			switch(match.getStatus()) {
			case EXACT:
				logger.info(scientificName + " matches " + match.getInternal().getScientificName());
				break;
			case PARTIAL:
				logger.warn("Partial match for " + scientificName + " to " + match.getInternal().getScientificName());
				break;			
			}
			return match.getInternal();
		} else {
			logger.warn(matches.size() + " matches for " + scientificName);
			return null;
		}
	}

	@Override
	public Description process(Item item) throws Exception {
		Description description = new Description();
		description.setDescription(translate(item));
		description.setTaxon(getTaxon(item.getDescription()));
		
		if(characterForLink != null) {
			Character character = deltaContext.getCharacter(characterForLink);
			Attribute attribute = item.getAttribute(character);
			String value = attribute.getValueAsString();
			if(value != null) {
			    // Assumes value is enclosed in angle brackets i.e. is a comment
			    value = value.substring(1, value.length() - 1);
			    description.setSource(linkPrefix + value + linkSuffix); 
			}
			
		}
		
		return description;
	}

}
