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
package org.emonocot.job.sitemap;

import static org.junit.Assert.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.emonocot.model.marshall.xml.StaxEventItemWriter;
import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;
import org.junit.Before;
import org.junit.Test;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.core.io.FileSystemResource;
import org.springframework.oxm.xstream.XStreamMarshaller;

public class StaxWriterTest {

	/**
	 *
	 */
	private StaxEventItemWriter writer;

	/**
	 *
	 */
	private FileSystemResource tempFile = new FileSystemResource("target/test-outputs/StaxWriterTest.xml");

	/**
	 * @param writer the writer to set
	 */
	public final void setWriter(StaxEventItemWriter writer) {
		this.writer = writer;
	}

	/**
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		writer = new StaxEventItemWriter();
		writer.setResource(new FileSystemResource("target/test-outputs/StaxWriterTest.xml"));
		writer.setRootTagName("urlset");
		XStreamMarshaller marshaller = new XStreamMarshaller();

		Map<String, Object> aliases = new HashMap<String, Object>();
		aliases.put("url", Url.class);
		marshaller.setAliases(aliases);
		writer.setMarshaller(marshaller);
		Map<String, String> attributes = new HashMap<String, String>();
		attributes.put("xmlns", "http://www.sitemaps.org/schemas/sitemap");
		writer.setRootElementAttributes(attributes);

	}

	/**
	 * @throws Exception
	 */
	@Test
	public void testWriteUrlToFile() throws Exception {
		String expectedXml = "<?xml version='1.0' encoding='UTF-8'?><urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap\"><url><loc>http://build.e-monocot.org/test/identifier</loc><lastmod>1986-07-11T22:15:00.000+01:00</lastmod></url></urlset>";
		ExecutionContext executionContext = new ExecutionContext();
		executionContext.put("query.string", "from Taxon");
		writer.open(executionContext);
		ArrayList<Url> items = new ArrayList<Url>();
		Url data = new Url();
		data.setLastmod(ISODateTimeFormat.dateTime().print(new DateTime(1986, 07, 11, 22, 15, 00, 00)));
		data.setLoc(new URL("http://build.e-monocot.org/test/identifier"));
		items.add(data);
		writer.write(items);
		writer.close();
		assertTrue("The file should exist and be readable", tempFile.exists() && tempFile.isReadable());
		assertEquals("The XML produced didn't match expected XML", expectedXml, FileUtils.readFileToString(tempFile.getFile()));
	}
}