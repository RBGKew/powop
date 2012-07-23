package org.emonocot.job.dwc;

import static org.hamcrest.collection.IsArrayContaining.hasItemInArray;
import static org.junit.Assert.assertThat;

import java.io.File;

import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/**
 *
 * @author ben
 *
 */
public class ScratchpadArchiveUnpackerTest {

   /**
    *
    */
   private Resource content = new ClassPathResource(
           "/org/emonocot/zingiberaceae.zip");

  /**
   *
   */
   private String unpackDirectoryName = "target/archive";
	   // System.getProperty("java.io.tmpdir") + "/archive";

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

        File unpackDirectory = new File(unpackDirectoryName + "/zingiberaceae");
        String[] actualFiles = unpackDirectory.list();
        String[] expectedFiles = new String[] {
                "description.txt",
                "distribution.txt",
                "image.txt",
                "meta.xml",
                "reference.txt",
                "specimen.txt",
                "classification.txt"
                };
        for (String expectedFile : expectedFiles) {
            assertThat(actualFiles,
                hasItemInArray(expectedFile));
        }
    }

}
