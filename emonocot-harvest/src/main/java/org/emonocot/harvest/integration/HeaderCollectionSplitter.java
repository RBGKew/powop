/**
 * 
 */
package org.emonocot.harvest.integration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.integration.Message;
import org.springframework.integration.message.GenericMessage;
import org.springframework.integration.splitter.AbstractMessageSplitter;

/**
 * @author jk00kg
 *
 */
public class HeaderCollectionSplitter extends AbstractMessageSplitter {

    /**
     * There is the strong and unverified assumption that this key has a Itrable header value
     */
    private String requestKey;
    
    /**
     * 
     */
    private String responseKey;

    /**
     * @param requestKey the requestKey to set
     */
    public void setRequestKey(String requestKey) {
        this.requestKey = requestKey;
    }

    /**
     * @param responseKey the responseKey to set
     */
    public void setResponseKey(String responseKey) {
        this.responseKey = responseKey;
    }

    /*
     * Splits a 'request' message, preserving the payload and all headers other than that named by the requestKey.
     * A message header containing the object from the Iterable can be added to a header on the 'response' message by setting the responseKey
     * @return A list of messages, one for each of the objects in the specified message header
     */
    @Override
    protected List<Message<Object>> splitMessage(Message<?> message) {
        List<Message<Object>> split = new ArrayList<Message<Object>>();
        Message<Object> m;
        for(Object o : (Iterable) message.getHeaders().get(requestKey)) {
            Map<String, Object> newHeaders = new HashMap<String, Object>();
            newHeaders.putAll(message.getHeaders());
            if(responseKey != null) {
                newHeaders.put(responseKey, o);
            }
            m = new GenericMessage<Object>(message.getPayload(), newHeaders);
            split.add(m);
        }
        return split;
    }

}
