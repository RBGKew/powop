package org.emonocot.job.dwc.write;

import java.io.IOException;
import java.io.Writer;

import org.springframework.batch.item.file.FlatFileHeaderCallback;

public class DwcHeaderWriter implements FlatFileHeaderCallback {
	
	String header = null;
	
	String delimiter = null;
	
	public void setHeader(String header) {
		this.header = header;
	}
	
	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}
	

	@Override
	public void writeHeader(Writer writer) throws IOException {
		writer.write(header.replace(",", delimiter));
	}

}
