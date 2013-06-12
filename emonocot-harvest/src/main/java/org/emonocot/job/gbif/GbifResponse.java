package org.emonocot.job.gbif;

import java.util.ArrayList;
import java.util.List;

public class GbifResponse {
	
	private Header header;
	
	private String exceptionReport;
	
    private List<DataProvider> dataProviders = new ArrayList<DataProvider>();

	public Header getHeader() {
		return header;
	}

	public void setHeader(Header header) {
		this.header = header;
	}

	public String getExceptionReport() {
		return exceptionReport;
	}

	public void setExceptionReport(String exceptionReport) {
		this.exceptionReport = exceptionReport;
	}

	public List<DataProvider> getDataProviders() {
		return dataProviders;
	}

	public void setDataProviders(List<DataProvider> dataProviders) {
		this.dataProviders = dataProviders;
	}

}
