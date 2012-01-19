package org.emonocot.harvest.messaging;

import java.util.concurrent.atomic.AtomicInteger;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author ben
 *
 */
@Component
public class JmsMessageListener implements MessageListener {

    /**
     *
     */
    private static Logger logger = LoggerFactory
            .getLogger(JmsMessageListener.class);

    /**
     *
     */
    @Autowired
    private AtomicInteger counter = null;

    /**
     * @param message Set the message
     * Implementation of <code>MessageListener</code>.
     */
    public final void onMessage(final Message message) {
        try {
            int messageCount = message
                    .getIntProperty(JmsMessageProducer.MESSAGE_COUNT);

            if (message instanceof TextMessage) {
                TextMessage tm = (TextMessage) message;
                String msg = tm.getText();

                logger.info("Processed message '{}'.  value={}", msg,
                        messageCount);

                counter.incrementAndGet();
            }
        } catch (JMSException e) {
            logger.error(e.getMessage(), e);
        }
    }

}
