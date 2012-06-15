package org.emonocot.job.taxonmatch;

import java.io.IOException;
import java.io.Writer;

import org.springframework.batch.item.file.FlatFileHeaderCallback;

/**
 *
 * @author ben
 *
 */
public class HeaderCallback implements FlatFileHeaderCallback {

    /**
     * @param writer set the writer
     * @throws IOException if there is a problem writing the header
     */
    public final void writeHeader(final Writer writer) throws IOException {
        writer.write("Original Identifier,Matched Identifier,Status,Number of Matches,Name,Iterpreted As,Basionym Authorship, Authorship");
    }

}
