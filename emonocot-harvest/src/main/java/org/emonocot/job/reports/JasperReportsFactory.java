/**
 * 
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

}
