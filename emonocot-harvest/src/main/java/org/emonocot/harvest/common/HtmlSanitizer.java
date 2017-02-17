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
package org.emonocot.harvest.common;

import org.apache.commons.lang.StringEscapeUtils;
import org.owasp.validator.html.AntiSamy;
import org.owasp.validator.html.CleanResults;
import org.owasp.validator.html.Policy;
import org.owasp.validator.html.PolicyException;
import org.owasp.validator.html.ScanException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class HtmlSanitizer {

	private Logger logger = LoggerFactory.getLogger(HtmlSanitizer.class);

	private Resource policyFile = new ClassPathResource("/META-INF/antisamy-policy.xml");

	private Policy policy = null;

	private AntiSamy antiSamy = new AntiSamy();

	public void setPolicyFile(Resource policyFile) {
		this.policyFile = policyFile;
	}

	public void afterPropertiesSet() throws Exception {
		policy = Policy.getInstance(policyFile.getURL());
	}

	public String sanitize(String unclean) {
		if (unclean == null || unclean.isEmpty()) {
			return unclean;
		} else if (unclean.matches(".*\\<[^>]+>.*")) {

			String unescaped = StringEscapeUtils.unescapeHtml(unclean);

			CleanResults cleanResults;
			try {
				cleanResults = antiSamy.scan(unescaped, policy);
				return cleanResults.getCleanHTML();
			} catch (PolicyException pe) {
				throw new RuntimeException(pe);
			} catch (ScanException se) {
				if (unclean.length() > 36) {
					logger.error("Could not sanitize html " + unclean.substring(0, 36), se);
					return null;
				} else {
					logger.error("Could not sanitize html " + unclean, se);
					return null;
				}
			} catch (Exception e) {
				logger.error("Could not sanitize html " + unclean, e);
				return null;
			}
		} else {
			String unescaped = StringEscapeUtils.unescapeHtml(unclean);
			return unescaped.replace("\0", "");
		}
	}

}
