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
    private String defaultTemplateName;
    
    /**
     * A mapping between logical template/view names used in this class and resolvable template locations 
     */
    private Map<String, String> templates;

    /**
     * @param defaultTemplateName the defaultTemplateName to set
     */
    public void setDefaultTemplateName(String defaultTemplateName) {
        this.defaultTemplateName = defaultTemplateName;
    }

    /**
     * @param templates the templates to set
     */
    public void setTemplates(Map<String, String> templates) {
        this.templates = templates;
    }
    
    /**
     * Create a comment replying to an incoming email
     * @param message
     * @return
     */
    public Comment createReply(Message<?> message) {
    	return null;
    }

    /**
     * Prepare a message for outgoing email
     * @param message
     * @return
     */
    public Message<Map> prepareMessage(Message<?> message) {
        Map<String, Object> model = new HashMap<String, Object>();
        Map<String, Object> headers = new HashMap<String, Object>();
        headers.putAll(message.getHeaders());
        Object payload = message.getPayload();
        if(payload instanceof Comment) {
            model.put("comment", payload);
            //Decide which template
            Base about = ((Comment) payload).getAboutData();
            String templateName = null;
            if (about instanceof BaseData) {
                templateName = "comment";
            }
            if(templateName != null) {
                headers.put(HEADER_TEMPLATE_NAME, templates.get(templateName));
            } else {
                headers.put(HEADER_TEMPLATE_NAME, templates.get(defaultTemplateName));
            }
        }
        return new GenericMessage<Map>(model, headers);
        
    }

}
