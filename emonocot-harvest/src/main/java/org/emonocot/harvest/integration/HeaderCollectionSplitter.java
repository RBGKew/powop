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
