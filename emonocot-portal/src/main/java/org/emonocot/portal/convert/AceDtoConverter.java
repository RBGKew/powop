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
package org.emonocot.portal.convert;

import org.emonocot.api.OrganisationService;
import org.emonocot.model.SecuredObject;
import org.emonocot.model.registry.Organisation;
import org.emonocot.portal.controller.form.AceDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;

/**
 *
 * @author ben
 *
 */
public class AceDtoConverter implements Converter<AceDto, SecuredObject> {

    /**
     *
     */
    private OrganisationService sourceService;

    /**
     *
     * @param sourceService Set the source service
     */
    @Autowired
    public final void setSourceService(final OrganisationService sourceService) {
        this.sourceService = sourceService;
    }

    /**
     * @param aceDto The DTO to convert
     * @return a secured object
     */
    public final SecuredObject convert(final AceDto aceDto) {
        if (aceDto.getClazz().equals(Organisation.class)) {
            return sourceService.find(aceDto.getObject());
        }
        return null;
    }

}
