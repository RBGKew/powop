/**
 * 
 */
package org.emonocot.integration;

import static org.junit.Assert.*;

import org.emonocot.harvest.integration.test.MessageHandler;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.ReadableDuration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author jk00kg
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:META-INF/spring/applicationContext-integrationTest.xml",
                       "classpath:META-INF/spring/applicationContext-integration.xml",
                       "classpath:META-INF/spring/applicationContext-test.xml"})
public class CommentSendingIntegrationTest {

    private Logger logger = LoggerFactory.getLogger(CommentSendingIntegrationTest.class);

    /**
     * 
     */
    @Autowired
    private MessageHandler listener;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    @Test
    public final void testPoller() {
        block(new Duration(5000));
        logger.info("There are " + listener.messages.size() + " messages");
    }
    
    /**
     * @param duration
     */
    public void block(ReadableDuration duration) {
        DateTime started = new DateTime();
        logger.info("There are " + listener.messages.size() + " messages");
        logger.info("Started at " + started);
        DateTime endAt = started.plus(duration);
        logger.info("Will end at " + endAt);
        while (endAt.isAfterNow()) {
            //wait
        }
        
    }

}
