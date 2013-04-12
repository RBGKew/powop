/**
 * 
 */
package org.emonocot.job.download;

import static org.junit.Assert.*;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.fill.JRVerticalReportWriter;

import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;
import org.apache.velocity.app.VelocityEngine;
/*import org.emonocot.job.jasperreports.JRTaxonBeanCollectionFactory;*/
import org.emonocot.model.Taxon;
import org.gbif.ecat.voc.TaxonomicStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ui.velocity.VelocityEngineFactoryBean;
import org.springframework.ui.velocity.VelocityEngineUtils;
import org.xml.sax.helpers.DefaultHandler;

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
    public final void test() {

    }
/* Rejected:
    Reports are likely to be requested in formats other than PDF
    The style of creating documents (With code rather than parameterised templates) is not consistent with
      use within eMonocot and only at Kew in older legacy systems.
    @Test
    public final void testITextWriter() throws Exception {
        Document document = new Document();
        // step 2
        PdfWriter.getInstance(document, new FileOutputStream("target/iTextHello.pdf"));
        // step 3
        document.open();
        // step 4
        String name = "Joe";
        document.add(new Paragraph("Hello " + name + "!"));
        for(Taxon t : itemsToWrite) {
    //protected void writeItem(Taxon item) {
            document.add(new Paragraph("Taxon " + t.getIdentifier() + " named " + t.toString() + " is a " + t.getTaxonomicStatus().toString()));
    //}
        }
        // step 5
        document.close();

    }
*/
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
        //The FillManager delegates to a JR(.+)Filler that usually has fillReportStart() fillReportContent() and fillReportEnd() methods
        //TODO Look at implementing writer/stream interface delegating to these methods. See {@link net.sf.jasperreports.engine.fill.JRVerticalFiller}
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
        writer.afterStep(se);//Add jasperreports.output.file=? to ExecutionContext
        
    }
    
    @Test
    @Ignore
    public final void testFOP() throws Exception {

/*        File outFile = null;
        OutputStream out = null;
        try {
            // Step 1: Construct a FopFactory
            // (reuse if you plan to render multiple documents!)
            FopFactory fopFactory = FopFactory.newInstance();

            // Step 2: Set up output stream.
            // Note: Using BufferedOutputStream for performance reasons (helpful
            // with FileOutputStreams).
            outFile = new File("target/fopHello.pdf");
            out = new BufferedOutputStream(new FileOutputStream(outFile));
            
            // Step 3: Construct fop with desired output format
            Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, out);

            // Step 4: Setup JAXP using identity transformer
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer(); // identity
                                                                // transformer

            // Step 5: Setup input and output for XSLT transformation
            // Setup input stream
            File foFile = new ClassPathResource("org/emonocot/job/download/fop/helloworld.fo").getFile();
            Source src = new StreamSource(foFile);

            
            //Different block
            System.out.println("FOP ExampleFO2PDFUsingSAXParser\n");
            System.out.println("Preparing...");

            //Setup input and output files

            System.out.println("Input: XSL-FO (" + foFile + ")");
            System.out.println("Output: PDF (" + outFile + ")");
            System.out.println();
            System.out.println("Transforming...");

            FOUserAgent foUserAgent = fopFactory.newFOUserAgent();
            // configure foUserAgent as desired

                // Construct fop and setup output format
                Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, out);

                // Setup SAX parser
                // throws FactoryConfigurationError
                SAXParserFactory factory = SAXParserFactory.newInstance();
                factory.setNamespaceAware(true);
                // throws ParserConfigurationException
                SAXParser parser = factory.newSAXParser();

                // Obtain FOP's DefaultHandler
                // throws FOPException
                DefaultHandler dh = fop.getDefaultHandler();

                // Start parsing and FOP processing
                // throws SAXException, IOException
                parser.parse(foFile, dh);

            System.out.println("Success!");
        } catch (Exception e) {
            e.printStackTrace(System.err);
            fail("There was an error: " + e.getMessage());
        } finally {
            out.close();
        }
*/        
        
        //Names:
        OutputStream nameOutStream = null;
        try {
            // Step 1: Construct a FopFactory
            // (reuse if you plan to render multiple documents!)
            FopFactory fopFactory = FopFactory.newInstance();

            // Step 2: Set up output stream.
            // Note: Using BufferedOutputStream for performance reasons (helpful
            // with FileOutputStreams).
            File outNames = new File("target/fopNames.pdf");
            nameOutStream = new BufferedOutputStream(new FileOutputStream(outNames));
            
            // Step 3: Construct fop with desired output format
            /*Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, out);

            // Step 4: Setup JAXP using identity transformer
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer(); // identity
                                                                // transformer
*/
            // Step 5: Setup input and output for XSLT transformation
            // Setup input stream
            Map<String, Object> names = new HashMap<String, Object>();
            names.put("items", itemsToWrite);
            Writer writer = new BufferedWriter(new FileWriter("target/fopNames.fo"));
            VelocityEngineUtils.mergeTemplate(velocity, "org/emonocot/job/download/fop/names.fo.vm", "utf-8", names, writer);
            File foFile = new File("target/fopNames.fo");
            
            //Different block
            System.out.println("FOP ExampleFO2PDFUsingSAXParser\n");
            System.out.println("Preparing...");

            //Setup input and output files

            System.out.println("Input: XSL-FO (" + foFile + ")");
            System.out.println("Output: PDF (" + outNames + ")");
            System.out.println();
            System.out.println("Transforming...");

            FOUserAgent foUserAgent = fopFactory.newFOUserAgent();
            // configure foUserAgent as desired

                // Construct fop and setup output format
                Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, nameOutStream);

                // Setup SAX parser
                // throws FactoryConfigurationError
                SAXParserFactory factory = SAXParserFactory.newInstance();
                factory.setNamespaceAware(true);
                // throws ParserConfigurationException
                SAXParser parser = factory.newSAXParser();

                // Obtain FOP's DefaultHandler
                // throws FOPException
                DefaultHandler dh = fop.getDefaultHandler();

                // Start parsing and FOP processing
                // throws SAXException, IOException
                parser.parse(foFile, dh);

            System.out.println("Success!");
        } catch (Exception e) {
            e.printStackTrace(System.err);
            fail("There was an error: " + e.getMessage());
        } finally {
            nameOutStream.close();
        }
        
    }
    

}
