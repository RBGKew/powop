/**
 * 
 */
package org.emonocot.harvest.integration.test;

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
public class MailSenderStubImpl implements JavaMailSender, EmailService {

    private Logger logger = LoggerFactory.getLogger(MailSenderStubImpl.class);

    /* (non-Javadoc)
     * @see org.emonocot.service.EmailService#sendEmail(java.lang.String, java.util.Map, java.lang.String, java.lang.String)
     */
    @Override
    public void sendEmail(String templateName, Map model, String toAddress,
            String subject) {
        logger .info("Recieved sendEmail with tamplate:" + templateName + ", model:" + model + ", to:" + toAddress + " subject:" + subject);
        
    }

    /* (non-Javadoc)
     * @see org.springframework.mail.MailSender#send(org.springframework.mail.SimpleMailMessage)
     */
    @Override
    public void send(SimpleMailMessage simpleMessage) throws MailException {
        logger.info("Recieved send with simple mail message:" + simpleMessage); 
    }

    /* (non-Javadoc)
     * @see org.springframework.mail.MailSender#send(org.springframework.mail.SimpleMailMessage[])
     */
    @Override
    public void send(SimpleMailMessage[] simpleMessages) throws MailException {
        logger.info("Recieved send with " + simpleMessages.length + " simple mail messages.");
        for (int i = 0; i < simpleMessages.length; i++) {
            logger.info("Message at index "+ i + " message:" + simpleMessages[i]);
        }
    }

    /* (non-Javadoc)
     * @see org.springframework.mail.javamail.JavaMailSender#createMimeMessage()
     */
    @Override
    public MimeMessage createMimeMessage() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.springframework.mail.javamail.JavaMailSender#createMimeMessage(java.io.InputStream)
     */
    @Override
    public MimeMessage createMimeMessage(InputStream contentStream)
            throws MailException {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.springframework.mail.javamail.JavaMailSender#send(javax.mail.internet.MimeMessage)
     */
    @Override
    public void send(MimeMessage mimeMessage) throws MailException {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see org.springframework.mail.javamail.JavaMailSender#send(javax.mail.internet.MimeMessage[])
     */
    @Override
    public void send(MimeMessage[] mimeMessages) throws MailException {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see org.springframework.mail.javamail.JavaMailSender#send(org.springframework.mail.javamail.MimeMessagePreparator)
     */
    @Override
    public void send(MimeMessagePreparator mimeMessagePreparator)
            throws MailException {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see org.springframework.mail.javamail.JavaMailSender#send(org.springframework.mail.javamail.MimeMessagePreparator[])
     */
    @Override
    public void send(MimeMessagePreparator[] mimeMessagePreparators)
            throws MailException {
        // TODO Auto-generated method stub
        
    }

}
