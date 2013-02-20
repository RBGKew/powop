/**
 * 
 */
package org.emonocot.integration;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.emonocot.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author jk00kg
 *
 */
public class MailSenderStubImpl implements EmailService {

    private Logger logger = LoggerFactory.getLogger(MailSenderStubImpl.class);

    /* (non-Javadoc)
     * @see org.emonocot.service.EmailService#sendEmail(java.lang.String, java.util.Map, java.lang.String, java.lang.String)
     */
    @Override
    public void sendEmail(String templateName, Map model, String toAddress,
            String subject) {
        logger.info("Received sendEmail with template:" + templateName + ", model:" + model + ", to:" + toAddress + " subject:" + subject);
        for(Object key : model.keySet()) {
            try {
                logger.info(key + " was " + BeanUtils.describe(model.get(key)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
    }

}
