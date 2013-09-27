/**
 * 
 */
package org.emonocot.service.impl;

import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

/**
 * @author jk00kg
 *
 */
public class JavaMailSenderImplFactory implements FactoryBean<JavaMailSender> {

    JavaMailSender sender;
    
    String username;
    
    String password;
    
    Properties javaMailProperties;
    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @param javaMailProperties the javaMailProperties to set
     */
    public void setJavaMailProperties(Properties javaMailProperties) {
        this.javaMailProperties = javaMailProperties;
    }

    /* (non-Javadoc)
     * @see org.springframework.beans.factory.FactoryBean#getObject()
     */
    @Override
    public JavaMailSender getObject() throws Exception {
        if(sender == null) {
            instantiate();
        }
        return sender;
    }

    /* (non-Javadoc)
     * @see org.springframework.beans.factory.FactoryBean#getObjectType()
     */
    @Override
    public Class<?> getObjectType() {
        return JavaMailSenderImpl.class;
    }

    /* (non-Javadoc)
     * @see org.springframework.beans.factory.FactoryBean#isSingleton()
     */
    @Override
    public boolean isSingleton() {
        return true;
    }

    /**
     * @return the instantiate JavaMailSender
     */
    private void instantiate() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        if(StringUtils.isNotBlank(username)) {
            javaMailSender.setUsername(username);
        }
        if(StringUtils.isNotBlank(password)) {
            javaMailSender.setPassword(password);
        }
        javaMailSender.setJavaMailProperties(javaMailProperties);
        sender = javaMailSender;
    }

}
