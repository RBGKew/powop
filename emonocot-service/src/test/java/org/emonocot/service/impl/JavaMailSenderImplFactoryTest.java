/**
 * 
 */
package org.emonocot.service.impl;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Properties;

import org.junit.Test;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

/**
 * @author jk00kg
 *
 */
public class JavaMailSenderImplFactoryTest {

    /**
     * 
     */
    @Test
    public void testGetObjectWithoutBlanks() throws Exception {
        JavaMailSenderImplFactory factory = new JavaMailSenderImplFactory();
        factory.setUsername("");
        factory.setPassword("");
        Properties props = new Properties();
        factory.setJavaMailProperties(props);
        JavaMailSenderImpl sender = (JavaMailSenderImpl) factory.getObject();
        assertNull("Username should be null, not blank", sender.getUsername());
        assertNull("Password should be null, not blank", sender.getPassword());
        assertEquals("There should be no properties", 0, sender.getJavaMailProperties().size());
    }
    
    @Test
    public void testGetObjectWithParams() throws Exception {
        JavaMailSenderImplFactory factory = new JavaMailSenderImplFactory();
        String user = "foo";
        factory.setUsername(user);
        String pass = "bar";
        factory.setPassword(pass);
        Properties props = new Properties();
        props.put(user, pass);
        factory.setJavaMailProperties(props);
        JavaMailSenderImpl sender = (JavaMailSenderImpl) factory.getObject();
        assertEquals("Username should be " + user, user, sender.getUsername());
        assertEquals("Password should be " + pass, pass, sender.getPassword());
        assertEquals("Properties should be set", props, sender.getJavaMailProperties());
    }

}
