package org.emonocot.harvest.media;

import org.emonocot.model.Image;
import org.emonocot.model.constants.AnnotationCode;

public interface ImageAnnotator {

	public abstract void annotate(Image image, AnnotationCode code,
			String message);

}