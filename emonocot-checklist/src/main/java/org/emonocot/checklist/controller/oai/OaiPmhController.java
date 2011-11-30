package org.emonocot.checklist.controller.oai;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sf.ehcache.Ehcache;

import org.emonocot.checklist.logging.LoggingConstants;
import org.emonocot.checklist.model.Family;
import org.emonocot.checklist.model.Taxon;
import org.emonocot.checklist.persistence.TaxonDao;
import org.openarchives.pmh.MetadataFormat;
import org.openarchives.pmh.MetadataPrefix;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author ben
 *
 */
@Controller
@RequestMapping("/oai")
public class OaiPmhController extends AbstractOaiPmhController<Taxon, TaxonDao> {

    /**
     * Logger for reporting, statistics etc.
     */
    private static Logger queryLog
        = LoggerFactory.getLogger("query");

   /**
    *
    */
   private static final String OAIPMH_WEBSERVICE_SEARCH_TYPE = "op";

    /**
     *
     * @param taxonDao Set the taxon dao used by this controller
     */
    @Autowired
    public final void setTaxonService(final TaxonDao taxonDao) {
        super.setService(taxonDao);
    }

    /**
     *
     * @param resumptionTokenCache the cache to store resumption tokens in
     */
    @Autowired
    public final void setResumptionTokenCache(
              final Ehcache resumptionTokenCache) {
        super.setCache(resumptionTokenCache);
    }

    @Override
    public final List<MetadataFormat> getMetadataFormats() {
        List<MetadataFormat> metadataFormats = super.baseMetadataFormat();
        MetadataFormat rdf = new MetadataFormat();
        rdf.setMetadataPrefix(MetadataPrefix.RDF);
        rdf.setMetadataNamespace("http://www.w3.org/1999/02/22-rdf-syntax-ns#");
        metadataFormats.add(rdf);
        return metadataFormats;
    }

    @Override
    public final Set<org.openarchives.pmh.Set> getSets() {
        Set<org.openarchives.pmh.Set> sets
            = new HashSet<org.openarchives.pmh.Set>();
        for (Family family : Family.values()) {
            org.openarchives.pmh.Set set = new org.openarchives.pmh.Set();
            set.setSetName(family.name());
            set.setSetSpec(family.name());
            sets.add(set);
        }

        for (Object[] result : getService().listSets()) {
            org.openarchives.pmh.Set set = new org.openarchives.pmh.Set();
            set.setSetName(result[1].toString());
            set.setSetSpec(result[0].toString() + ":" + result[1].toString());
            sets.add(set);
        }
        return sets;
    }

    @Override
    public final void log(final Serializable identifier, final String verb,
            final int count, final String set) {
        try {
            MDC.put(LoggingConstants.SEARCH_TYPE_KEY,
                    OAIPMH_WEBSERVICE_SEARCH_TYPE);
            if (identifier != null) {
                MDC.put(LoggingConstants.QUERY_KEY, identifier.toString());
            }
            if (set != null) {
                MDC.put(LoggingConstants.FAMILY_KEY, set);
            }
            MDC.put(LoggingConstants.RESULT_COUNT_KEY, Integer.toString(count));
            queryLog.info("OaiPmhController." + verb);
        } finally {
            MDC.remove(LoggingConstants.SEARCH_TYPE_KEY);
            MDC.remove(LoggingConstants.QUERY_KEY);
            MDC.remove(LoggingConstants.RESULT_COUNT_KEY);
            MDC.remove(LoggingConstants.FAMILY_KEY);
        }
    }
}
