package org.emonocot.job.map;

public class TMSLayer extends Layer {
	
	private String layer = "";
	
	public TMSLayer() {
		super.setType("TMS");
	}

	public String getLayer() {
		return layer;
	}

	public void setLayer(String layer) {
		this.layer = layer;
	}

}
