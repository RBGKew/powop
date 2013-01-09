package org.emonocot.model.convert;

import org.emonocot.api.OrganisationService;
import org.emonocot.model.registry.Organisation;
import org.springframework.core.convert.converter.Converter;

public class StringToOrganisationConverter implements Converter<String, Organisation> {

	private OrganisationService organisationService;
	
	public void setOrganisationService(OrganisationService organisationService) {
		this.organisationService = organisationService;
	}


	@Override
	public Organisation convert(String source) {
		if(source == null) {
			return null;
		}
		return organisationService.load(source);
	}

}
