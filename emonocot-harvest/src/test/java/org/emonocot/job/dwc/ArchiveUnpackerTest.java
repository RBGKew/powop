package org.emonocot.job.dwc;

import java.io.File;
import static org.junit.Assert.assertArrayEquals;

import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/**
 *
 * @author ben
 *
 */
public class ArchiveUnpackerTest {

   /**
    *
    */
   private Resource content = new ClassPathResource(
           "/org/emonocot/job/dwc/test.zip");

  /**
   *
   */
   private String unpackDirectoryName = System.getProperty("java.io.tmpdir")
   + "/archive";

   /**
    *
    */
   private ArchiveUnpacker archiveUnpacker = new ArchiveUnpacker();

    /**
     * @throws Exception if there is a problem accessing the file
     */
    @Test
    public final void testUnpack() throws Exception {

        archiveUnpacker.unpackArchive(content.getFile().getAbsolutePath(),
                unpackDirectoryName);

        File unpackDirectory = new File(unpackDirectoryName);
        String[] actualFiles = unpackDirectory.list();
        String[] expectedFiles = new String[] {
                "description.txt",
                "distribution.txt",
                "eml.xml",
                "images.txt",
                "meta.xml",
                "taxa.txt"};
        assertArrayEquals("ArchiveUnpacker should unpack the expected files",
                expectedFiles, actualFiles);
    }

}
