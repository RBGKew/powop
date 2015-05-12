/*
 * This is eMonocot, a global online biodiversity information resource.
 *
 * Copyright © 2011–2015 The Board of Trustees of the Royal Botanic Gardens, Kew and The University of Oxford
 *
 * eMonocot is free software: you can redistribute it and/or modify it under the terms of the
 * GNU Affero General Public License as published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * eMonocot is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * The complete text of the GNU Affero General Public License is in the source repository as the file
 * ‘COPYING’.  It is also available from <http://www.gnu.org/licenses/>.
 */
package org.emonocot.job.taxonmatch;

import java.io.IOException;
import java.io.Writer;

import org.emonocot.model.Taxon;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.ItemProcessListener;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.file.FlatFileFooterCallback;

/**
 *
 * @author ben
 *
 */
public class StatusTallyingFooterCallback implements ItemProcessListener<Taxon, Result>, StepExecutionListener,
        FlatFileFooterCallback {

    /**
     *
     */
    private int cannotParse = 0;

    /**
     *
     */
    private int noExactMatches = 0;

    /**
     *
     */
    private int multipleMatches = 0;

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
    public void beforeProcess(final Taxon item) {

    }

    /**
     * @param item the item being processed
     * @param result the result of the processing
     */
    public final void afterProcess(final Taxon item, final Result result) {
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
        case MULTIPLE_MATCHES:
            multipleMatches++;
            break;
        case CANNOT_PARSE:
            cannotParse++;
            break;
        default:
            break;
        }
    }

    /**
     * @param item the item being processed
     * @param e the exception thrown
     */
    public void onProcessError(final Taxon item, final Exception e) {

    }

    /**
     * @param writer Set the writer
     * @throws IOException if there is a problem
     */
    public final void writeFooter(final Writer writer) throws IOException {
        writer.write("\nSummary,\n");
        if (singleMatches > 0) {
            writer.write("Exact Match," + singleMatches + ",\n");
        }
        if (multipleMatches > 0) {
            writer.write("Multipe Exact Matches," + multipleMatches + ",\n");
        }
        if (noExactMatches > 0) {
            writer.write("Only Partial Matches," + noExactMatches + ",\n");
        }
        if (noMatches > 0) {
            writer.write("No Match," + noMatches + ",\n");
        }
        if (cannotParse > 0) {
            writer.write("Could not understand," + cannotParse + ",\n");
        }
    }

    /* (non-Javadoc)
     * @see org.springframework.batch.core.StepExecutionListener#beforeStep(org.springframework.batch.core.StepExecution)
     */
    @Override
    public void beforeStep(StepExecution stepExecution) {
        //Initialise totals
        singleMatches = 0;
        multipleMatches = 0;
        noExactMatches = 0;
        noMatches = 0;
        cannotParse = 0;
    }

    /* (non-Javadoc)
     * @see org.springframework.batch.core.StepExecutionListener#afterStep(org.springframework.batch.core.StepExecution)
     */
    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        return stepExecution.getExitStatus();
    }

}
