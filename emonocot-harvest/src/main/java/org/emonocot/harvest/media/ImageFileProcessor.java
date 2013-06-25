package org.emonocot.harvest.media;

import org.emonocot.model.Image;

public interface ImageFileProcessor {

	/**
	 * @param image Set the image
	 * @return an image or null if the image is to be skipped
	 * @throws Exception if there is a problem processing the image
	 */
	public abstract Image process(final Image image) throws Exception;

}