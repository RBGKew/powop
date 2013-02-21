/**
 * 
 */
package org.emonocot.integration;

import java.util.HashMap;
import java.util.Map;

import org.emonocot.model.Base;
import org.emonocot.model.BaseData;
import org.emonocot.model.Comment;
import org.springframework.integration.Message;
import org.springframework.integration.message.GenericMessage;

/**
 * @author jk00kg
 *
 */
public class EmailServiceHelper {
    
    /**
     * 
     */
    private final String HEADER_TEMPLATE_NAME = "templateName";

    /**
     * 
     */
    private String commentEmailTemplate;
    
    /**
     * 
     */
    private String defaultTemplate;

    /**
     * @param commentEmailTemplate the commentEmailTemplate to set
     */
    public void setCommentEmailTemplate(String commentEmailTemplate) {
        this.commentEmailTemplate = commentEmailTemplate;
    }

    /**
     * @param defaultTemplate the defaultTemplate to set
     */
    public void setDefaultTemplate(String defaultTemplate) {
        this.defaultTemplate = defaultTemplate;
    }

    public Message<Map> prepareMessage(Message<?> message) {
        Map<String, Object> model = new HashMap<String, Object>();
        Map<String, Object> headers = new HashMap<String, Object>();
        headers.putAll(message.getHeaders());
        Object payload = message.getPayload();
        if(payload instanceof Comment) {
            model.put("comment", payload);
            //Decide which template
            Base about = ((Comment) payload).getAboutData();
            if (about instanceof BaseData) {
                headers.put(HEADER_TEMPLATE_NAME, commentEmailTemplate);
            } else {
                headers.put(HEADER_TEMPLATE_NAME, defaultTemplate);
            }
        }
        return new GenericMessage<Map>(model, headers);
        
    }

}
