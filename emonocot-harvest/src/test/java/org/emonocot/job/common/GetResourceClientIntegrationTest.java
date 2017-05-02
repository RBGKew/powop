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

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

import org.emonocot.harvest.common.GetResourceClient;
import org.joda.time.DateTime;
import org.joda.time.base.BaseDateTime;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.RetryListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.xml.sax.SAXException;

import com.github.tomakehurst.wiremock.junit.WireMockRule;

public class GetResourceClientIntegrationTest {

	private static final BaseDateTime PAST_DATETIME = new DateTime(2010, 11, 1, 9, 0, 0, 0);

	private final int mockHttpPort = 8088;
	private final String mockHttpUrl = "http://localhost:" + mockHttpPort;

	@Rule
	public WireMockRule wireMockRule = new WireMockRule(mockHttpPort);

	private Logger logger = LoggerFactory.getLogger(GetResourceClientIntegrationTest.class);

	private GetResourceClient getResourceClient;

	@Before
	public final void setUp() throws IOException {
		getResourceClient = new GetResourceClient();
		stubFor(get(urlEqualTo("/dwc.zip"))
				.willReturn(aResponse()
						.withStatus(200)
						.withBodyFile("/dwc.zip")));
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

		ExitStatus exitStatus = getResourceClient.getResource(mockHttpUrl + "/dwc.zip",
				Long.toString(PAST_DATETIME.getMillis()),
				tempFile.getAbsolutePath());

		assertNotNull("ExitStatus should not be null", exitStatus);
		assertEquals("ExitStatus should be COMPLETED", ExitStatus.COMPLETED, exitStatus);
	}

	/**
	 * This works on a normal apache httpd directory, but not for GIT
	 * @throws IOException
	 *             if a temporary file cannot be created or if there is a http
	 *             protocol error.
	 */
	@Test
	@Ignore
	public final void testGetResourceNotModified() throws IOException {
		File tempFile = File.createTempFile("test", "zip");
		tempFile.deleteOnExit();

		ExitStatus exitStatus = getResourceClient.getResource(mockHttpUrl + "/dwc.zip",
				Long.toString(new Date().getTime() - 60000L),
				tempFile.getAbsolutePath());

		assertNotNull("ExitStatus should not be null", exitStatus);
		assertEquals("ExitStatus should be NOT_MODIFIED", "NOT_MODIFIED", exitStatus.getExitCode());
	}

	/**
	 *
     @throws IOException
	 *             if a temporary file cannot be created or if there is a http
	 *             protocol error.
	 */
	@Test
	@Ignore
	public final void testGetDocumentAnyOtherStatus() throws IOException {
		AttemptCountingRetryListener retryListener = new AttemptCountingRetryListener();
		File tempFile = File.createTempFile("test", "zip");
		tempFile.deleteOnExit();
		getResourceClient.setRetryListeners(new RetryListener[] { retryListener });

		ExitStatus exitStatus = getResourceClient.getResource("http://not.a.domain.invalid/test.zip",
						Long.toString(new Date().getTime()),
						tempFile.getAbsolutePath());

		assertNotNull("ExitStatus should not be null.", exitStatus);
		assertEquals("ExitStatus should be FAILED.", ExitStatus.FAILED, exitStatus);
		assertEquals("There should be three retry attempts.", 3, retryListener.getErrors());
	}

	class AttemptCountingRetryListener implements RetryListener {
		private int errors = 0;

		public int getErrors() {
			return errors;
		}

		@Override
		public <T, E extends Throwable> boolean open(RetryContext context, RetryCallback<T, E> callback) {
			return false;
		}

		@Override
		public <T, E extends Throwable> void close(RetryContext context, RetryCallback<T, E> callback, Throwable throwable) { }

		@Override
		public <T, E extends Throwable> void onError(RetryContext context, RetryCallback<T, E> callback, Throwable throwable) {
			logger.info("Got error number " + ++errors, throwable);
		}
	}
}
