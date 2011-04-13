package org.emonocot.job.scratchpads.convert;

import org.emonocot.job.scratchpads.model.EoLDataObject;
import org.emonocot.model.common.License;
import org.emonocot.model.description.Feature;
import org.emonocot.model.description.TextContent;
import org.emonocot.service.DescriptionService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;

public class EoLTextDataObjectConverter implements Converter<EoLDataObject,TextContent>{
	
	private DescriptionService descriptionService;
	
	private ConversionService conversionService;
	
	@Autowired
	public void setConversionService(ConversionService conversionService) {
		this.conversionService = conversionService;
	}
	
	@Autowired
	public void setDescriptionService(DescriptionService descriptionService) {
		this.descriptionService = descriptionService;
	}

	public TextContent convert(EoLDataObject dataObject) {
		TextContent textContent = new TextContent();
		textContent.setCreated(conversionService.convert(dataObject.getCreated(), DateTime.class));
		textContent.setModified(conversionService.convert(dataObject.getModified(), DateTime.class));
		textContent.setLicense(conversionService.convert(dataObject.getLicense(),License.class));
		textContent.setSource(dataObject.getSource());
		textContent.setTaxon(dataObject.getTaxon());
		textContent.setContent(dataObject.getDescription());
		textContent.setFeature(conversionService.convert(dataObject.getSubject(), Feature.class));
		if(dataObject.getAgent() != null && dataObject.getAgent().getRole().equals("author")) {
		  textContent.setCreator(dataObject.getAgent().getURI());
		}
		
		if(dataObject.getTaxon().getId() != null) {
			TextContent persistedTextContent = descriptionService.getTextContent(textContent.getFeature(),textContent.getTaxon());
			
			if(persistedTextContent != null && persistedTextContent.equals(textContent)) {
				return persistedTextContent;
			}
			
		}
		return textContent;
	}

}
