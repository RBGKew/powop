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
package org.emonocot.integration;

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
    public void sendEmail(String templateName, Map model, String toAddress, String subject) {
        logger.info("Received sendEmail with template:" + templateName + ", model:" + model + ", to:" + toAddress + " subject:" + subject);
        for(Object key : model.keySet()) {
            try {
                logger.info(key + " was " + BeanUtils.describe(model.get(key)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
    }

    /* (non-Javadoc)
     * @see org.emonocot.service.EmailService#sendEmail(java.lang.String, java.util.Map, java.lang.String[], java.lang.String)
     */
    @Override
    public void sendEmail(String templateName, Map model, String[] toAddress, String subject) {
        logger.info("Recieved sendEmail with multiple addresses");
        sendEmail(templateName, model, toAddress.toString(), subject);
        
    }

}
