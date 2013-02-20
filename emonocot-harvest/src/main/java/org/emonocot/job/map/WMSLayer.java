package org.emonocot.job.map;

import java.util.HashMap;
import java.util.Map;

public class WMSLayer extends Layer {
	
	private String[] layers = new String[0];
	
	private String[] styles = new String[0];
	

	
	public WMSLayer() {
		super.setType("WMS");
	}

	public String[] getLayers() {
		return layers;
	}

	public void setLayers(String[] layers) {
		this.layers = layers;
	}

	public String[] getStyles() {
		return styles;
	}

	public void setStyles(String[] styles) {
		this.styles = styles;
	}

}
