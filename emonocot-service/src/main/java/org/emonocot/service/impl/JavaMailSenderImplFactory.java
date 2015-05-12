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
