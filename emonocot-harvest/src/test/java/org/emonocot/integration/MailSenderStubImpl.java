/**
 * 
 */
package org.emonocot.integration;

import java.io.InputStream;
import java.util.Map;

import javax.mail.internet.MimeMessage;

import org.emonocot.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;

/**
 * @author jk00kg
 *
 */
public class MailSenderStubImpl implements EmailService {

    private Logger logger = LoggerFactory.getLogger(MailSenderStubImpl.class);

    /* (non-Javadoc)
     * @see org.emonocot.service.EmailService#sendEmail(java.lang.String, java.util.Map, java.lang.String, java.lang.String)
     */
    @Override
    public void sendEmail(String templateName, Map model, String toAddress,
            String subject) {
        logger .info("Recieved sendEmail with tamplate:" + templateName + ", model:" + model + ", to:" + toAddress + " subject:" + subject);
        
    }

}
