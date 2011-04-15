package org.emonocot.job.scratchpads.convert;

import org.emonocot.job.scratchpads.model.EoLDataObject;
import org.emonocot.model.common.License;
import org.emonocot.model.description.Feature;
import org.emonocot.model.description.TextContent;
import org.emonocot.service.DescriptionService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;

public class EoLTextDataObjectConverter implements Converter<EoLDataObject,TextContent>{
	
	private DescriptionService descriptionService;
	
	private Converter<String,DateTime> dateTimeConverter;
	
	private Converter<String,License> licenseConverter;
	
	private Converter<String,Feature> featureConverter;
	
	@Autowired
	public void setDescriptionService(DescriptionService descriptionService) {
		this.descriptionService = descriptionService;
	}

	@Autowired
	public void setDateTimeConverter(Converter<String, DateTime> dateTimeConverter) {
		this.dateTimeConverter = dateTimeConverter;
	}

	@Autowired
	public void setLicenseConverter(Converter<String, License> licenseConverter) {
		this.licenseConverter = licenseConverter;
	}

	@Autowired
	public void setFeatureConverter(Converter<String, Feature> featureConverter) {
		this.featureConverter = featureConverter;
	}

	public TextContent convert(EoLDataObject dataObject) {
		TextContent textContent = new TextContent();
		textContent.setCreated(dateTimeConverter.convert(dataObject.getCreated()));
		textContent.setModified(dateTimeConverter.convert(dataObject.getModified()));
		textContent.setLicense(licenseConverter.convert(dataObject.getLicense()));
		textContent.setSource(dataObject.getSource());
		textContent.setTaxon(dataObject.getTaxon());
		textContent.setContent(dataObject.getDescription());
		textContent.setFeature(featureConverter.convert(dataObject.getSubject()));
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
