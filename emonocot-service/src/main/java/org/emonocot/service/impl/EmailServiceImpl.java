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
package org.emonocot.service.impl;

import java.util.Map;
import java.util.regex.Pattern;

import javax.mail.internet.MimeMessage;

import org.apache.velocity.app.VelocityEngine;
import org.emonocot.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.ui.velocity.VelocityEngineUtils;

public class EmailServiceImpl implements EmailService {
	
    /**
     * Email addresses are expected to be separated by commas, semi-colons, spaces or any combination thereof 
     */
    protected static final Pattern ADDRESS_DELIMITERS = Pattern.compile("(,|;| )+");
    
    private JavaMailSender mailSender;
	
    private VelocityEngine velocityEngine;
    
    private String fromAddress;
    
    @Autowired
    public void setMailSender(JavaMailSender mailSender) {
    	this.mailSender = mailSender;
    }
    
    @Autowired
    public void setVelocityEngine(VelocityEngine velocityEngine) {
    	this.velocityEngine = velocityEngine;
    }
    
    public void setFromAddress(String fromAddress) {
    	this.fromAddress = fromAddress;
    }

	@Override
	public void sendEmail(final String templateName, final Map model, final String toAddress, final String subject) {
		sendEmail(templateName, model, ADDRESS_DELIMITERS.split(toAddress), subject);
	}
	
	public void sendEmail(final String templateName, final Map model, final String[] toAddresses, final String subject) {
	    MimeMessagePreparator preparator = new MimeMessagePreparator() {
            public void prepare(MimeMessage mimeMessage) throws Exception {
                MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
                message.setTo(toAddresses);
                message.setFrom(fromAddress);
                message.setSubject(subject);
                String text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, templateName, model);
                message.setText(text, true);
            }
         };
         this.mailSender.send(preparator);
     }

}
