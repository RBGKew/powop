/**
 * 
 */
package org.emonocot.integration;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.Message;
import org.springframework.integration.MessageChannel;
import org.springframework.integration.channel.ChannelInterceptor;

/**
 * @author jk00kg
 * @param <T>
 *
 */
public class MessageHandler<T> implements ChannelInterceptor {

    private Logger logger = LoggerFactory.getLogger(MessageHandler.class);
    
    /**
     * 
     */
    public List<Object> messages = new ArrayList<Object>();
    
    /**
     * 
     */
    private MessageChannel primaryChannel;
    
    /**
     * @return the primaryChannel
     */
    public MessageChannel getPrimaryChannel() {
        return primaryChannel;
    }

    /**
     * @param primaryChannel the primaryChannel to set
     */
    public void setPrimaryChannel(MessageChannel primaryChannel) {
        this.primaryChannel = primaryChannel;
    }

    /**
     * @param message
     * @throws Exception 
     */
    public void handle(Message<T> message) throws Exception {
        handle(message, null, null);
    }
    
    /**
     * @param message
     * @throws Exception 
     */
    public void handle(Message<?> message, MessageChannel channel, Boolean sent) {
        if(message != null) {
            logger.info("Got a message:");
            logger.info(message.toString());
            messages.add(message);
            logger.info("The Payload was:");
            try {
                logger.info(message.getPayload().toString() + ":" + BeanUtils.describe(message.getPayload()));
            } catch (Exception e) {
                logger.error("Error trying to describe the payload.", e);
            }
            logger.info("There are now " + messages.size() + " messages.");
        } else {
            logger.info("Got a null message.");
        }
        /*if(channel != null) {
            logger.info("With channel: " + channel);
            if(channel.equals(primaryChannel)) {
                messages.add(message);
            }
        }*/
    }

    /* (non-Javadoc)
     * @see org.springframework.integration.channel.ChannelInterceptor#postReceive(org.springframework.integration.Message, org.springframework.integration.MessageChannel)
     */
    @Override
    public Message<?> postReceive(Message<?> message, MessageChannel channel) {
        handle(message, channel, null);
        return message;
    }

    /* (non-Javadoc)
     * @see org.springframework.integration.channel.ChannelInterceptor#postSend(org.springframework.integration.Message, org.springframework.integration.MessageChannel, boolean)
     */
    @Override
    public void postSend(Message<?> message, MessageChannel channel, boolean sent) {
        handle(message, channel, sent);
    }

    /* (non-Javadoc)
     * @see org.springframework.integration.channel.ChannelInterceptor#preReceive(org.springframework.integration.MessageChannel)
     */
    @Override
    public boolean preReceive(MessageChannel channel) {
        return true;
    }

    /* (non-Javadoc)
     * @see org.springframework.integration.channel.ChannelInterceptor#preSend(org.springframework.integration.Message, org.springframework.integration.MessageChannel)
     */
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        handle(message, channel, null);
        return message;
    }

}
