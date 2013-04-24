/**
 * 
 */
package org.emonocot.job.download;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.fill.JRVerticalReportWriter;

import org.apache.velocity.app.VelocityEngine;
/*import org.emonocot.job.jasperreports.JRTaxonBeanCollectionFactory;*/
import org.emonocot.model.Taxon;
import org.gbif.ecat.voc.TaxonomicStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ui.velocity.VelocityEngineFactoryBean;

/**
 * @author jk00kg
 * 
 */
public class PdfWritingTest {
    
    VelocityEngine velocity;

    /**
     * 
     */
    List<Taxon> itemsToWrite;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        
        VelocityEngineFactoryBean velocityFactory = new VelocityEngineFactoryBean();
        velocityFactory.setConfigLocation(new ClassPathResource("org/emonocot/job/download/fop/velocity.properties"));
        velocityFactory.afterPropertiesSet();
        velocity = velocityFactory.getObject();
        itemsToWrite = new ArrayList<Taxon>();
        for (int i = 0; i < 5; i++) {
            Taxon t = new Taxon();
            t.setTaxonomicStatus(TaxonomicStatus.Accepted);
            t.setScientificName(t.getTaxonomicStatus().toString() + i);
            t.setIdentifier(t.getScientificName());
            itemsToWrite.add(t);
        }
        for (int i = 0; i < 15; i++) {
            Taxon t = new Taxon();
            t.setTaxonomicStatus(TaxonomicStatus.Synonym);
            int mod = i%3;
            t.setAcceptedNameUsage(itemsToWrite.get(mod));
            t.setScientificName(t.getTaxonomicStatus().toString() + i);
            t.setIdentifier(t.getScientificName());
            itemsToWrite.add(t);
        } /*JRTaxonBeanCollectionFactory.create();*/
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    @Test
    public final void testJasperReportsWriter() throws Exception {

        JasperReport jasperReport = JasperCompileManager.compileReport(
                new ClassPathResource("org/emonocot/job/download/reports/jasperreports_demo.jrxml").getInputStream());
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("name", "Joe");
        JRDataSource jrDatasource = new JREmptyDataSource();
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, jrDatasource);
        JasperExportManager.exportReportToPdfFile(jasperPrint, "target/jrHello.pdf");
        
        //With the data-source
        JRDataSource jrNameDatasource = new JRBeanCollectionDataSource(itemsToWrite);
        JasperReport jrNameReport = JasperCompileManager.compileReport(
                new ClassPathResource("org/emonocot/job/download/reports/name_report1.jrxml").getInputStream());
        //The FillManager delegates to a 'Filler' that have fillReportStart(), fillReportContent() and fillReportEnd() methods
        JasperPrint jrNamePrint = JasperFillManager.fillReport(jrNameReport, params, jrNameDatasource); 
        JasperExportManager.exportReportToPdfFile(jrNamePrint, "target/jrNames.pdf");
    }
    
    @Test
    public final void testBatchReportWriter() throws Exception {

        JasperReport jasperReport = JasperCompileManager.compileReport(
                new ClassPathResource("org/emonocot/job/download/reports/name_report1.jrxml").getInputStream());
        JRVerticalReportWriter writer = new JRVerticalReportWriter(jasperReport);
        writer.setDefaultOutputDir("target");
        StepExecution se = new StepExecution("testStep", new JobExecution(1L));
        writer.beforeStep(se);
        int chunkSize = 10;
        for (int i = 0; i <= (itemsToWrite.size()/chunkSize); i++) {
            List<Taxon> itemList = new ArrayList<Taxon>();
            for (int j = 0; j < chunkSize; j++) {
                try {
                    itemList.add(itemsToWrite.get(i*chunkSize+j));
                } catch (IndexOutOfBoundsException e) {
                    break;
                }
            }
            writer.write(itemList);
            
        }
        writer.afterStep(se);
        
    }

}
