package org.emonocot.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.mail.internet.MimeMessage;

import org.apache.velocity.app.VelocityEngine;
import org.emonocot.service.EmailService;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.ui.velocity.VelocityEngineUtils;

public class EmailServiceImpl implements EmailService {
	
	private JavaMailSender mailSender;
	
    private VelocityEngine velocityEngine;
    
    private String fromAddress;
    
    public void setJavaMailSender(JavaMailSender mailSender) {
    	this.mailSender = mailSender;
    }
    
    public void setVelocityEngine(VelocityEngine velocityEngine) {
    	this.velocityEngine = velocityEngine;
    }
    
    public void setFromAddress(String fromAddress) {
    	this.fromAddress = fromAddress;
    }

	@Override
	public void sendEmail(final String templateName, final Map model, final String toAddress) {
		MimeMessagePreparator preparator = new MimeMessagePreparator() {
	           public void prepare(MimeMessage mimeMessage) throws Exception {
	              MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
	              message.setTo(toAddress);
	              message.setFrom(fromAddress); 	              
	              String text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, templateName, model);
	              message.setText(text, true);
	           }
	        };
	        this.mailSender.send(preparator);
	}

}
