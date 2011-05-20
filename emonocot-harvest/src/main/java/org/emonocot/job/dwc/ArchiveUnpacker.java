package org.emonocot.job.dwc;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;

/**
 *
 * @author ben
 *
 */
public class ArchiveUnpacker {
    /**
    *
    */
    private Logger logger = LoggerFactory.getLogger(ArchiveUnpacker.class);

    /**
    *
    */
    static final int BUFFER = 2048;

    /**
     *
     * @param archiveName
     *            The name of the archive file to unpack
     * @param unpackDirectory
     *            The directory to unpack the file into
     * @return an exit status indicating whether this
     *         step was successfully completed
     */
    public final ExitStatus unpackArchive(final String archiveName,
            final String unpackDirectory) {
        try {
            BufferedOutputStream bufferedOutputStream = null;
            BufferedInputStream bufferedInputStream = null;
            ZipEntry entry;
            ZipFile zipfile = new ZipFile(archiveName);
            Enumeration entries = zipfile.entries();
            while (entries.hasMoreElements()) {
                entry = (ZipEntry) entries.nextElement();
                logger.debug("Extracting: " + entry + " from " + archiveName);
                bufferedInputStream = new BufferedInputStream(
                        zipfile.getInputStream(entry));
                int count;
                byte[] data = new byte[BUFFER];
                FileOutputStream fileOutputStream = new FileOutputStream(
                        entry.getName());
                bufferedOutputStream = new BufferedOutputStream(
                        fileOutputStream, BUFFER);
                while (
                       (count
                          = bufferedInputStream.read(data, 0, BUFFER)) != -1) {
                    bufferedOutputStream.write(data, 0, count);
                }
                bufferedOutputStream.flush();
                bufferedOutputStream.close();
                bufferedInputStream.close();
            }
        } catch (IOException ioe) {
            logger.error("Input Output Exception unpacking "
                    + archiveName + " " + ioe.getLocalizedMessage());
            return ExitStatus.FAILED;
        }

        return ExitStatus.COMPLETED;
    }

}
