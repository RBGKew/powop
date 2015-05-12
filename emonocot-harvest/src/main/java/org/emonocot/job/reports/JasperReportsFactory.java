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
package org.emonocot.job.reports;

import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;

/**
 * @author jk00kg
 *
 */
public class JasperReportsFactory {
    
    private static Logger logger = LoggerFactory.getLogger(JasperReportsFactory.class);

    public static JasperReport compileReport(InputStream in) {
        
        try {
            return JasperCompileManager.compileReport(in);
        } catch (JRException e) {
            logger.error("Unable to create instance of JasperReport", e);
            return null;
        }
    }
    
    public static JasperReport compileReportFromFilePath(String filePath) {
        Resource resource = new ClassPathResource(filePath);
        try {
            return JasperCompileManager.compileReport(resource.getInputStream());
        } catch (JRException e) {
            logger.error("Unable to create instance of JasperReport for " + filePath, e);
            return null;
        } catch (IOException e) {
            logger.error("Unable to create instance of JasperReport for " + filePath, e);
            return null;
        }
        
    }
    
    public static JasperReport getObject(String filePath) {
        return compileReportFromFilePath(filePath);
    }

}
