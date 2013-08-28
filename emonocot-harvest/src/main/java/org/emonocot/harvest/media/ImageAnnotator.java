package org.emonocot.harvest.media;

import org.emonocot.model.Image;
import org.emonocot.model.constants.AnnotationCode;
import org.emonocot.model.constants.AnnotationType;

public interface ImageAnnotator {

	public abstract void annotate(Image image, AnnotationType annotationType, AnnotationCode code, String message);

}