package org.emonocot.portal.logging;

/**
 *
 * @author ben
 *
 */
public final class LoggingConstants {
    /**
     * The client name.
     */
    public static final String MDC_CLIENT_NAME_KEY
        = "org.emonocot.checklist.logging.LoggingConstants.clientName";

    /**
     * The type of search: 'te' for Taxon Extractor and 'op' for OAI-PMH.
     */
    public static final String SEARCH_TYPE_KEY
        = "org.emonocot.checklist.logging.LoggingConstants.searchType";

    /**
     *
     */
    public static final String FAMILY_KEY
        = "org.emonocot.checklist.logging.LoggingConstants.family";

    /**
     *
     */
    public static final String QUERY_KEY
        = "org.emonocot.checklist.logging.LoggingConstants.query";

    /**
     *
     */
    public static final String RESULT_COUNT_KEY
        = "org.emonocot.checklist.logging.LoggingConstants.resultCount";

    /**
     *
     */
    private LoggingConstants() { }

}
