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
package org.emonocot.job.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;
import org.easymock.EasyMock;
import org.emonocot.harvest.common.GetResourceClient;
import org.junit.Before;
import org.junit.Test;
import org.springframework.batch.core.ExitStatus;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.xml.sax.SAXException;

/**
 *
 * @author ben
 *
 */
public class GetResourceClientTest {

	private GetResourceClient getResourceClient = new GetResourceClientMockedHttp();

	private HttpClient httpClient = EasyMock.createMock(HttpClient.class);

	private BasicHttpResponse httpResponse = new BasicHttpResponse(new BasicStatusLine(HttpVersion.HTTP_1_0, HttpStatus.SC_OK, "OK"));

	private Resource content = new ClassPathResource("/__files/dwc.zip");

	// Only a mock URL.
	private final String testzip = "http://build.e-monocot.org/test/test.zip";

	private class GetResourceClientMockedHttp extends GetResourceClient {
		@Override
		public void prepareHttpClient() { }
	}

	/**
	 *
	 * @throws IOException
	 *             if the test file cannot be found
	 */
	@Before
	public final void setUp() throws IOException {
		getResourceClient.setHttpClient(httpClient);
		httpResponse.setEntity(new FileEntity(content.getFile(),
				ContentType.create("application/zip")));
	}

	/**
	 *
	 * @throws IOException
	 *             if a temporary file cannot be created or if there is a http
	 *             protocol error.
	 * @throws SAXException
	 *             if the content retrieved is not valid xml.
	 */
	@Test
	public final void testGetResourceSuccessfully() throws IOException,
	SAXException {
		File tempFile = File.createTempFile("test", "zip");
		tempFile.deleteOnExit();

		EasyMock.expect(httpClient.execute(EasyMock.isA(HttpGet.class)))
		.andReturn(httpResponse);
		EasyMock.replay(httpClient);

		ExitStatus exitStatus = getResourceClient
				.getResource(testzip,
						tempFile.getAbsolutePath(),
						Long.toString(new Date().getTime()));

		EasyMock.verify(httpClient);

		assertNotNull("ExitStatus should not be null", exitStatus);
		assertEquals("ExitStatus should be COMPLETED", exitStatus,
				ExitStatus.COMPLETED);
	}

	/**
	 *
	 * @throws IOException
	 *             if a temporary file cannot be created or if there is a http
	 *             protocol error.
	 */
	@Test
	public final void testGetResourceNotModified() throws IOException {
		File tempFile = File.createTempFile("test", "zip");
		tempFile.deleteOnExit();
		httpResponse.setStatusLine(new BasicStatusLine(HttpVersion.HTTP_1_0,
				HttpStatus.SC_NOT_MODIFIED, "Not Modified"));

		EasyMock.expect(httpClient.execute(EasyMock.isA(HttpGet.class)))
		.andReturn(httpResponse);
		EasyMock.replay(httpClient);

		ExitStatus exitStatus = getResourceClient
				.getResource(testzip,
						tempFile.getAbsolutePath(),
						Long.toString(new Date().getTime()));

		EasyMock.verify(httpClient);

		assertNotNull("ExitStatus should not be null", exitStatus);
		assertEquals("ExitStatus should be NOT_MODIFIED",
				exitStatus.getExitCode(), "NOT_MODIFIED");
	}

	/**
	 *
     @throws IOException
	 *             if a temporary file cannot be created or if there is a http
	 *             protocol error.
	 */
	@Test
	public final void testGetDocumentAnyOtherStatus() throws IOException {
		File tempFile = File.createTempFile("test", "zip");
		tempFile.deleteOnExit();
		httpResponse.setStatusLine(new BasicStatusLine(HttpVersion.HTTP_1_0,
				HttpStatus.SC_BAD_REQUEST, "Bad Request"));

		EasyMock.expect(httpClient.execute(EasyMock.isA(HttpGet.class)))
		.andReturn(httpResponse).anyTimes();
		EasyMock.replay(httpClient);

		ExitStatus exitStatus = getResourceClient
				.getResource(testzip,
						tempFile.getAbsolutePath(),
						Long.toString(new Date().getTime()));

		EasyMock.verify(httpClient);

		assertNotNull("ExitStatus should not be null", exitStatus);
		assertEquals("ExitStatus should be FAILED", exitStatus,
				ExitStatus.FAILED);
	}
}
