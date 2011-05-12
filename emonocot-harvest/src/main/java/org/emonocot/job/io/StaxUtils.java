package org.emonocot.job.io;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;

import org.springframework.xml.transform.StaxResult;
import org.springframework.xml.transform.StaxSource;

/**
 * <p>Handles Spring XML difference between Spring WS 1.* and Spring 3.</p>
 */
class StaxUtils {
	private static String SPRING_3_AVAILABLE = "org.springframework.util.xml.StaxUtils";
	private static Boolean isSpring3Available;
	
	public static Source createStaxSource (XMLEventReader eventReader) {
		if (! isSpring3Available()) {
			return createSourceWithSpringWs(eventReader);
		} else {
			return createSourceWithSpring(eventReader);
		}
	}

	private static Source createSourceWithSpring(XMLEventReader eventReader) {
		try {
			return org.springframework.util.xml.StaxUtils.createStaxSource(eventReader);
		} catch (XMLStreamException e) {
			throw new RuntimeException("Error while reading xml record", e);
		} 
	}
	
	private static Source createSourceWithSpringWs(XMLEventReader eventReader) {
		return new StaxSource(eventReader);
	}
	
	public static Result createStaxResult (XMLEventWriter eventWriter) {
		if (! isSpring3Available()) {
			return createResultWithSpringWs(eventWriter);
		} else {
			return createResultWithSpring(eventWriter);
		}
	}

	private static Result createResultWithSpring(XMLEventWriter eventWriter) {
		try {
			return org.springframework.util.xml.StaxUtils.createStaxResult(eventWriter);
		} catch (XMLStreamException e) {
			throw new RuntimeException("Error while reading xml record", e);
		} 
	}
	
	private static Result createResultWithSpringWs(XMLEventWriter eventWriter) {
		return new StaxResult(eventWriter);
	}
	
	private static synchronized boolean isSpring3Available() {
		if (isSpring3Available == null) {
			try {
				Class.forName(SPRING_3_AVAILABLE, true, Thread.currentThread().getContextClassLoader());
				isSpring3Available = true;
			} catch (ClassNotFoundException err) {
				isSpring3Available = false;
			}
		}
		return isSpring3Available;
	}
}
