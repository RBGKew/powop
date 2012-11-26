package org.emonocot.model.geography;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import com.spatial4j.core.io.LineReader;


public class SampleDataReader extends LineReader<SampleData> {
	public SampleDataReader(InputStream r) throws IOException {
		super(r);
	}

	public SampleDataReader(Reader r) throws IOException {
		super(r);
	}

	public SampleDataReader(File f) throws IOException {
		super(f);
	}

	@Override
	public SampleData parseLine(String line) {
		return new SampleData(line);
	}

}
