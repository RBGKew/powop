/**
 * 
 */
package org.emonocot.integration;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.Message;

/**
 * @author jk00kg
 * @param <T>
 *
 */
public class MessageHandler<T> {

    private Logger logger = LoggerFactory.getLogger(MessageHandler.class);
    
    /**
     * 
     */
    public List<Object> messages = new ArrayList<Object>();
    
    /**
     * @param message
     * @throws NoSuchMethodException 
     * @throws InvocationTargetException 
     * @throws IllegalAccessException 
     */
    public void handle(Message<T> message) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        if(message != null) {
            logger.info("Got a message:");
            logger.info(message.toString());
            messages.add(message);
            logger.info("The Payload was:");
            logger.info(message.getPayload().toString() + ":" + BeanUtils.describe(message.getPayload()));
            logger.info("There are now " + messages.size() + " messages.");
        } else {
            logger.info("Got a null message.");
        }
    }

}
