package org.emonocot.job.scratchpads.convert;

import org.emonocot.job.scratchpads.model.EoLDataObject;
import org.emonocot.model.media.Image;
import org.springframework.core.convert.converter.Converter;

/**
 *
 * @author ben
 *
 */
public class EoLStillImageDataObjectConverter implements
        Converter<EoLDataObject, Image> {

    /**
     * @param image An eol data object to convert.
     * @return An image object.
     */
    public final Image convert(final EoLDataObject image) {
        // TODO Auto-generated method stub
        return null;
    }

}
