/**
 * 
 */
package org.emonocot.harvest.integration.test;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author jk00kg
 *
 */
public class MessageHandler {

    private Logger logger = LoggerFactory.getLogger(MessageHandler.class);
    
    /**
     * 
     */
    public List<Object> messages = new ArrayList<Object>();
    
    /**
     * @param message
     */
    public void handle(Object message) {
        if(message != null) {
            logger.info("Got a message:");
            logger.info(message.toString());
            messages.add(message);
            logger.info("There are now " + messages.size() + " messages.");
        } else {
            logger.info("Got a null message :-(");
        }
    }

}
