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
		policy = Policy.getInstance(policyFile.getFile());		
	}

	public String sanitize(String text) {
		if(text == null || text.isEmpty()) {
			return null;
		} else {
			String unescaped = StringEscapeUtils.unescapeHtml(text);
			try {
				CleanResults cleanResults = antiSamy.scan(unescaped, policy);
				return cleanResults.getCleanHTML();
			} catch (ScanException se) {
				if(unescaped.length() > 32) {
					throw new RuntimeException("Exception sanitizing html " + unescaped.substring(0,32),se);
				} else {
					throw new RuntimeException("Exception sanitizing html " + unescaped,se);
				}
			} catch (PolicyException pe) {
				throw new RuntimeException("Policy Exception sanitizing html",pe);
			}
		}
		
	}

}
