/*
 * This is eMonocot, a global online biodiversity information resource.
 *
 * Copyright © 2011–2015 The Board of Trustees of the Royal Botanic Gardens, Kew and The University of Oxford
 *
 * eMonocot is free software: you can redistribute it and/or modify it under the terms of the
 * GNU Affero General Public License as published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * eMonocot is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * The complete text of the GNU Affero General Public License is in the source repository as the file
 * ‘COPYING’.  It is also available from <http://www.gnu.org/licenses/>.
 */
package org.powo.model.geography;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Iterator;

/*
 * com.spatial4j.core.io.LineReader was removed in spatial4j 0.4. This is mostly a lift-and-shift of 
 * that code.
 */
public class SampleDataReader implements Iterator<SampleData> {
	private int count = 0;
	private int lineNumber = 0;
	private BufferedReader reader;
	private String nextLine;

	protected void readComment( String line ) { }

	public SampleDataReader(InputStream in) throws IOException {
		reader = new BufferedReader(new InputStreamReader( in, "UTF-8" ) );
		next();
	}

	public SampleDataReader(Reader r) throws IOException {
		if (r instanceof BufferedReader) {
			reader = (BufferedReader) r;
		} else {
			reader = new BufferedReader(r);
		}
		next();
	}

	public SampleDataReader(File f) throws IOException {
		reader = new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF-8"));
		next();
	}

	@Override
	public boolean hasNext() {
		return nextLine != null;
	}

	@Override
	public SampleData next() {
		SampleData val = null;
		if (nextLine != null) {
			val = parseLine(nextLine);
			count++;
		}

		if (reader != null) {
			try {
				while( reader != null ) {
					nextLine = reader.readLine();
					lineNumber++;
					if (nextLine == null ) {
						reader.close();
						reader = null;
					}
					else if( nextLine.startsWith( "#" ) ) {
						readComment( nextLine );
					}
					else {
						nextLine = nextLine.trim();
						if( nextLine.length() > 0 ) {
							break;
						}
					}
				}
			} catch (IOException ioe) {
				throw new RuntimeException("IOException thrown while reading/closing reader", ioe);
			}
		}
		return val;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

	public int getLineNumber() {
		return lineNumber;
	}

	public int getCount() {
		return count;
	}

	public SampleData parseLine(String line) {
		return new SampleData(line);
	}

}
