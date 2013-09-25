package org.emonocot.portal.controller.form;

public class NcbiDto {
	
	private String pubMedEntries;
	
	private String nucleotideEntries;
	
	private String protienEntries;

	public String getPubMedEntries() {
		return pubMedEntries;
	}

	public void setPubMedEntries(String string) {
		this.pubMedEntries = string;
	}

	public String getNucleotideEntries() {
		return nucleotideEntries;
	}

	public void setNucleotideEntries(String nucleotideEntries) {
		this.nucleotideEntries = nucleotideEntries;
	}

	public String getProtienEntries() {
		return protienEntries;
	}

	public void setProtienEntries(String protienEntries) {
		this.protienEntries = protienEntries;
	}
}
