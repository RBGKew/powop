package org.emonocot.portal.messaging;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class JmsMessageListenerIntegrationTest {

    final Logger logger = LoggerFactory.getLogger(JmsMessageListenerIntegrationTest.class);

    @Autowired
    private AtomicInteger counter = null;
    
    @Test
    public void testMessage() throws Exception {
        assertNotNull("Counter is null.", counter);

        int expectedCount = 100;
        
        logger.info("Testing...");
        
        // give listener a chance to process messages
        Thread.sleep(2 * 1000);
        
        assertEquals("Message is not '" + expectedCount + "'.", expectedCount, counter.get());
    }
    
}
