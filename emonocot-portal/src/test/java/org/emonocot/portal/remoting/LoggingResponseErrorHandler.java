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
package org.emonocot.portal.remoting;

import java.io.IOException;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;

/**
 *
 * @author ben
 *
 */
public class LoggingResponseErrorHandler extends DefaultResponseErrorHandler {
	/**
	 *
	 */
	private Logger logger = LoggerFactory
			.getLogger(LoggingResponseErrorHandler.class);
	/**
	 *
	 */
	static final int BUFFER = 2048;

	@Override
	public final void handleError(final ClientHttpResponse clientHttpResponse)
			throws IOException {
		InputStreamReader inputStreamReader = new InputStreamReader(
				clientHttpResponse.getBody());
		StringBuffer stringBuffer = new StringBuffer();
		char[] data = new char[BUFFER];
		while (inputStreamReader.read(data, 0, BUFFER) != -1) {
			stringBuffer.append(data);
		}

		inputStreamReader.close();
		logger.error(stringBuffer.toString());
	}
}
