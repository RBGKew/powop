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
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.fill.JRVerticalReportWriter;

import org.emonocot.model.Taxon;
import org.gbif.ecat.voc.TaxonomicStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.core.io.ClassPathResource;

/**
 * @author jk00kg
 * 
 */
public class PdfWritingTest {

    /**
     * 
     */
    List<Taxon> itemsToWrite;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        itemsToWrite = new ArrayList<Taxon>();
        for (int i = 0; i < 5; i++) {
            Taxon t = new Taxon();
            t.setTaxonomicStatus(TaxonomicStatus.Accepted);
            t.setScientificName(t.getTaxonomicStatus().toString() + i);
            t.setLicense("http://example.com/34*");
            t.setIdentifier(t.getScientificName());
            itemsToWrite.add(t);
        }
        itemsToWrite.get(4).setLicense("Ask Joseph nicely, preferably with cake");
        for (int i = 0; i < 15; i++) {
            Taxon t = new Taxon();
            t.setTaxonomicStatus(TaxonomicStatus.Synonym);
            int mod = i%3;
            Taxon a = itemsToWrite.get(mod);
            a.getSynonymNameUsages().add(t);
            t.setAcceptedNameUsage(a);
            t.setScientificName(t.getTaxonomicStatus().toString() + i);
            t.setIdentifier(t.getScientificName());
            itemsToWrite.add(t);
        }
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    @Test
    public final void testJasperReportsWriter() throws Exception {

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("name", "Joe");
        
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
