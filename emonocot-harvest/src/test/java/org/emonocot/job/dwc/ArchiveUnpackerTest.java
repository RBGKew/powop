package org.emonocot.job.dwc;

import static org.hamcrest.collection.IsArrayContaining.hasItemInArray;
import static org.junit.Assert.assertThat;

import java.io.File;

import org.emonocot.job.dwc.read.ArchiveUnpacker;
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
                "taxa.txt"
                };
        for (String expectedFile : expectedFiles) {
            assertThat(actualFiles,
                hasItemInArray(expectedFile));
        }
    }

}
