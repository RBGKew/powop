package org.emonocot.ws.checklist;

import org.springframework.batch.core.ExitStatus;

/**
 *
 * @author ben
 *
 */
public class OaiPmhClient {

    /**
     *
     * @param authorityName
     *            The name of the authority being harvested.
     * @param authorityURI
     *            The endpoint (uri) being harvested.
     * @param dateLastHarvested
     *            The dateTime when this authority was last harvested.
     * @param temporaryFileName
     *            The name of the temporary file to store the response in.
     * @param resumptionToken The resumption token if present.
     * @return An exit status indicating that the step was completed, failed, or
     *         if the authority responded with a NO RECORDS MATCH response
     *         indicating that no records have been modified
     */
    public final ExitStatus listRecords(final String authorityName,
            final String authorityURI, final String dateLastHarvested,
            final String temporaryFileName, final String resumptionToken) {
        return new ExitStatus("NO RECORDS MATCH");
    }

}
