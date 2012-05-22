package org.emonocot.model.hibernate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Iterator;

import com.vividsolutions.jts.geom.GeometryFactory;

import com.vividsolutions.jts.io.WKTReader;

/**
 *
 * @author ben
 *
 */
public class SimplifyingDataReader implements Iterator<SimplifiedData> {

    /**
     *
     */
    private int count = 0;
    /**
     *
     */
    private int lineNumber = 0;
    /**
     *
     */
    private BufferedReader reader;
    /**
     *
     */
    private String nextLine;

    /**
     *
     */
    private GeometryFactory factory = new GeometryFactory();

    /**
     *
     * @param line Set the line
     */
    protected void readComment(final String line) {

    }

    /**
     * @return true if there is a next line
     */
    public final boolean hasNext() {
        return nextLine != null;
    }

    /**
     * @return a SimplfiedData object
     */
    public final SimplifiedData next() {
        SimplifiedData val = null;
        if (nextLine != null) {
            val = parseLine(nextLine);
            count++;
        }

        if (reader != null) {
            try {
                while (reader != null) {
                    nextLine = reader.readLine();
                    lineNumber++;
                    if (nextLine == null) {
                        reader.close();
                        reader = null;
                    } else if (nextLine.startsWith("#")) {
                        readComment(nextLine);
                    } else {
                        nextLine = nextLine.trim();
                        if (nextLine.length() > 0) {
                            break;
                        }
                    }
                }
            } catch (IOException ioe) {
                throw new RuntimeException(
                        "IOException thrown while reading/closing reader", ioe);
            }
        }
        return val;
    }

    /**
     * Not supported.
     */
    public final void remove() {
        throw new UnsupportedOperationException();
    }

    /**
     *
     * @return the line number
     */
    public final int getLineNumber() {
        return lineNumber;
    }

    /**
     *
     * @return the count
     */
    public final int getCount() {
        return count;
    }

    /**
     *
     * @param inputStream
     *            Set the input stream
     * @throws IOException
     *             if there is a problem
     */
    public SimplifyingDataReader(final InputStream inputStream)
            throws IOException {
        reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        next();
    }

    /**
     *
     * @param reader
     *            Set the reader
     * @throws IOException
     *             if there is a problem
     */
    public SimplifyingDataReader(Reader reader) throws IOException {
        if (reader instanceof BufferedReader) {
            reader = (BufferedReader) reader;
        } else {
            reader = new BufferedReader(reader);
        }
        next();
    }

    /**
     *
     * @param file
     *            Set the file
     * @throws IOException
     *             if there is a problem
     */
    public SimplifyingDataReader(final File file) throws IOException {
        reader = new BufferedReader(new InputStreamReader(
                new FileInputStream(file), "UTF-8"));
        next();
    }

    /**
     * @param line
     *            The line to be parsed
     * @return sample data
     */
    public final SimplifiedData parseLine(final String line) {

        WKTReader wktReader = new WKTReader(factory);

        return new SimplifiedData(line, wktReader);
    }
}
