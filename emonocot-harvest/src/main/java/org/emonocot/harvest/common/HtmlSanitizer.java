package org.emonocot.harvest.common;

import org.apache.commons.lang.StringEscapeUtils;
import org.owasp.validator.html.AntiSamy;
import org.owasp.validator.html.CleanResults;
import org.owasp.validator.html.Policy;
import org.owasp.validator.html.PolicyException;
import org.owasp.validator.html.ScanException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class HtmlSanitizer {
	
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
		if(unclean == null || unclean.isEmpty()) {
			return unclean;
		}
		String unescaped = StringEscapeUtils.unescapeHtml(unclean);
		
		CleanResults cleanResults;
		try {
			cleanResults = antiSamy.scan(unescaped, policy);
			return cleanResults.getCleanHTML();
		} catch (PolicyException pe) {
			throw new RuntimeException(pe);
		} catch (ScanException se) {
			if(unclean.length() > 36) {
			    throw new RuntimeException("Could not sanitize html " + unclean.substring(0,36), se);
			} else {
				throw new RuntimeException("Could not sanitize html " + unclean, se);
			}
		}
	}

}
