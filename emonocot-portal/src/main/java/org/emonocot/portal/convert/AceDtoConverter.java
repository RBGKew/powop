package org.emonocot.portal.convert;

import org.emonocot.api.SourceService;
import org.emonocot.model.SecuredObject;
import org.emonocot.model.Source;
import org.emonocot.portal.model.AceDto;
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
    private SourceService sourceService;

    /**
     *
     * @param sourceService Set the source service
     */
    @Autowired
    public final void setSourceService(final SourceService sourceService) {
        this.sourceService = sourceService;
    }

    /**
     * @param aceDto The DTO to convert
     * @return a secured object
     */
    public final SecuredObject convert(final AceDto aceDto) {
        if (aceDto.getClazz().equals(Source.class)) {
            return sourceService.find(aceDto.getObject());
        }
        return null;
    }

}
