package org.emonocot.job.key;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

/**
 *
 * @author ben
 *
 */
public class KeyTransformationTest {

    /**
     *
     */
    private Resource inputFile = new ClassPathResource(
            "org/emonocot/job/sdd/testKey.xml");

   /**
    *
    */
    private Resource xsltFile = new ClassPathResource(
            "org/emonocot/job/key/sddToJSON.xsl");

    /**
     *
     */
    private Resource taxonNameFile = new ClassPathResource(
            "org/emonocot/job/sdd/taxon-file.xml");

    /**
     *
     */
   private Resource imageFile = new ClassPathResource(
           "org/emonocot/job/sdd/image-file.xml");


    /**
     *
     */
    private XmlTransformingTasklet xmlTransformingTasklet;

    /**
     * @throws IOException if we cannot create a tmp file
     */
    @Before
    public final void setUp() throws IOException {
        xmlTransformingTasklet = new XmlTransformingTasklet();
        xmlTransformingTasklet.setInputFile(inputFile);
        xmlTransformingTasklet.setXsltFile(xsltFile);
        File output = File.createTempFile("output", "json");
        FileSystemResource outputFile = new FileSystemResource(output);
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("taxonFileName", taxonNameFile.getFile()
                .getAbsolutePath());
        parameters.put("imageFileName", imageFile.getFile()
                .getAbsolutePath());
        xmlTransformingTasklet.setParameters(parameters);
        xmlTransformingTasklet.setOutputFile(outputFile);
    }

    /**
     * @throws Exception if there is a problem running the test
     */
    @Test
    public final void testKeyTransformation() throws Exception {
        xmlTransformingTasklet.execute(null, null);
    }

}
