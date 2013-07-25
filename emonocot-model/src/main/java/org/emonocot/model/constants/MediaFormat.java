package org.emonocot.model.constants;

public enum MediaFormat {
	txt(false),
	xml(false),
	jpg(true),
	png(true),
	gif(true);
	
	private boolean image = false;
	
	private MediaFormat(boolean image) {
		this.image = image;
	}
	
	public boolean isImage() {
		return image;
	}
}
