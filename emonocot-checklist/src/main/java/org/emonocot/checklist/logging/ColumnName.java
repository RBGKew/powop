package org.emonocot.checklist.logging;

/**
 *
 * @author ben
 *
 */
public enum ColumnName {
    /**
     * The primary key of this table (int).
     */
    web_log_id,
    /**
     * The date the search was performed (datetime).
     */
    search_date,
    /**
     * The IP address of the client (varchar(15)).
     */
    ip_address,
    /**
     * The type of search (varchar(2), qs, av).
     */
    search_type,
    /**
     * The result count (int).
     */
    result_count,
    /**
     * The family searched for (varchar(30)).
     */
    family,
    /**
     * The genus searched for (varchar(30)).
     */
    genus,
    /**
     * The species searched for (varchar(50)).
     */
    species,
    /**
     * The author searched for (varchar(60)).
     */
    author,
    /**
     * The infraspecific rank searched for (varchar(15)).
     */
    infra_rank,
    /**
     * The infraspecific epithet searched for (varchar(50)).
     */
    infra_epithet,
    /**
     * The publication searched for (varchar(60)).
     */
    publication,
    /**
     * The date published searched for (varchar(4)).
     */
    published,
    /**
     * The TDWG codes searched for (varchar(255)).
     */
    tdwg_codes,
    /**
     * The checklist searched for (varchar(30)).
     */
    checklist,
    /**
     * The referrer (varchar(50)).
     */
    req_referer,
    /**
     * The host (varchar(50)).
     */
    req_host,
    /**
     * The remote host (varchar(50)).
     */
    remote_host
}
