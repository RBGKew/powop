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

	public void ignore(Message<T> message) throws Exception {
		logger.info("Ignoring message " + message.getPayload());
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
