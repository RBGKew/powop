package org.emonocot.portal.integration;

import java.io.IOException;

import org.codehaus.jackson.map.ObjectMapper;
import org.emonocot.api.job.JobExecutionInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.Message;
import org.springframework.integration.message.GenericMessage;

/**
 *
 * @author ben
 *
 */
public class JobStatusNotificationTransformer {

   /**
    *
    */
    private static Logger logger = LoggerFactory
            .getLogger(JobStatusNotificationTransformer.class);

    /**
     *
     */
    private ObjectMapper objectMapper;

    /**
     *
     * @param objectMapper
     *            Set the object mapper
     */
    public final void setObjectMapper(final ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     *
     * @param message
     *            the http response
     * @return an the message
     * @throws IOException if there is a problem parsing the message
     */
    public final Message<JobExecutionInfo> transform(
            final Message<String> message) throws IOException {
        logger.debug("In JobStatusNotificationTransformer.transform (" + message + ")");
        logger.debug("Message payload is " + message.getPayload());
        JobExecutionInfo response = null;
        try {
            response = objectMapper.readValue(message.getPayload(),
                    JobExecutionInfo.class);
            logger.debug("Successfully translated object" + response);
            return new GenericMessage<JobExecutionInfo>(response);
        } catch (Exception e) {
            logger.debug(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

}
