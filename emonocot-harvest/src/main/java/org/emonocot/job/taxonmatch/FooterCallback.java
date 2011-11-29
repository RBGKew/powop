package org.emonocot.job.taxonmatch;

import java.io.IOException;
import java.io.Writer;

import org.springframework.batch.core.ItemProcessListener;
import org.springframework.batch.item.file.FlatFileFooterCallback;

/**
 *
 * @author ben
 *
 */
public class FooterCallback implements ItemProcessListener<TaxonDTO, Result>,
        FlatFileFooterCallback {

    /**
     *
     */
    private int noExactMatches = 0;
    /**
     *
     */
    private int noMatches = 0;
    /**
     *
     */
    private int singleMatches = 0;

    /**
     * @param item the item being processed
     */
    public void beforeProcess(final TaxonDTO item) {

    }

    /**
     * @param item the item being processed
     * @param result the result of the processing
     */
    public final void afterProcess(final TaxonDTO item, final Result result) {
        switch(result.getStatus()) {
        case SINGLE_MATCH:
            singleMatches++;
            break;
        case NO_MATCH:
            noMatches++;
            break;
        case NO_EXACT_MATCH:
            noExactMatches++;
            break;
        default:
            break;
        }
    }

    /**
     * @param item the item being processed
     * @param e the exception thrown
     */
    public void onProcessError(final TaxonDTO item, final Exception e) {

    }

    /**
     * @param writer Set the writer
     * @throws IOException if there is a problem
     */
    public final void writeFooter(final Writer writer) throws IOException {
        writer.write("\nSummary,\n");
        writer.write("Exact Match," + singleMatches + ",\n");
        writer.write("Multiple Matches," + noExactMatches + ",\n");
        writer.write("No Match," + noMatches + ",\n");
    }

}
