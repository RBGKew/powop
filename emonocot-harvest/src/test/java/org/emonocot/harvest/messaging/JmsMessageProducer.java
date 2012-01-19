package org.emonocot.harvest.messaging;

import javax.annotation.PostConstruct;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

/**
 *
 * @author ben
 *
 */
@Component
public class JmsMessageProducer {

    /**
     *
     */
    private static Logger logger = LoggerFactory
            .getLogger(JmsMessageProducer.class);

    /**
     *
     */
    protected static final String MESSAGE_COUNT = "messageCount";

    /**
     *
     */
    @Autowired
    private JmsTemplate template = null;

    /**
     *
     */
    private int messageCount = 100;

    /**
     * Generates JMS messages.
     * @throws JMSException if there is a problem sending a JMS message
     */
    @PostConstruct
    public final void generateMessages() throws JMSException {
        for (int i = 0; i < messageCount; i++) {
            final int index = i;
            final String text = "Message number is " + i + ".";

            template.send(new MessageCreator() {
                public Message createMessage(final Session session)
                        throws JMSException {
                    TextMessage message = session.createTextMessage(text);
                    message.setIntProperty(MESSAGE_COUNT, index);

                    logger.info("Sending message: " + text);

                    return message;
                }
            });
        }
    }

}
