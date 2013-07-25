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
		policy = Policy.getInstance(policyFile.getFile().getAbsolutePath());
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
					logger.error(
							"Could not sanitize html "
									+ unclean.substring(0, 36), se);
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
			return unclean;
		}
	}

}
