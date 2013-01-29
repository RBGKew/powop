package org.emonocot.service;

import java.util.Map;

public interface EmailService {
	public void sendEmail(String templateName, Map model, String toAddress, String subject);
}
