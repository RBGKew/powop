package org.emonocot.checklist.controller.oai;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sf.ehcache.Ehcache;

import org.emonocot.checklist.logging.LoggingConstants;
import org.emonocot.checklist.model.ChangeEvent;
import org.emonocot.checklist.model.ChangeEventImpl;
import org.emonocot.checklist.model.ChangeType;
import org.emonocot.checklist.model.Family;
import org.emonocot.checklist.model.Rank;
import org.emonocot.checklist.model.Taxon;
import org.emonocot.checklist.persistence.TaxonDao;
import org.joda.time.DateTime;
import org.openarchives.pmh.IdDoesNotExistException;
import org.openarchives.pmh.MetadataFormat;
import org.openarchives.pmh.MetadataPrefix;
import org.openarchives.pmh.Verb;
import org.openarchives.pmh.format.annotation.MetadataPrefixFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author ben
 *
 */
@Controller
@RequestMapping("/oai")
public class OaiPmhController
    extends AbstractOaiPmhController<Taxon, TaxonDao> {

    /**
     *
     */
    private static final String FAMILY_ID_PREFIX = "urn:kew.org:wcs:family:";

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

    /**
     * Get a single record.
     *
     * @param identifier The identifier of the record
     * @param metadataPrefix The metadata prefix for the output
     * @return a model and view which can then be rendered
     * @throws IdDoesNotExistException
     *             if the id cannot be found in the repository
     */
    @Override
    @RequestMapping(method = RequestMethod.GET, params = "verb=GetRecord")
    public final ModelAndView getRecord(
            @RequestParam(value = "identifier", required = true)
                final Serializable identifier,
            @RequestParam(value = "metadataPrefix", required = true)
            @MetadataPrefixFormat
                final MetadataPrefix metadataPrefix)
            throws IdDoesNotExistException {
        if (identifier.toString().startsWith(FAMILY_ID_PREFIX)) {
            try {
                Long familyId = Long.parseLong(identifier.toString().substring(
                        FAMILY_ID_PREFIX.length()));
                Family family = Family.fromIdentifier(-familyId);
                ModelAndView modelAndView = new ModelAndView();
                modelAndView.addObject(
                        AbstractOaiPmhController.REQUEST_KEY,
                        constructRequest(Verb.GetRecord, null, null, null, null,
                                metadataPrefix));
                log(identifier, "GetRecord", 1, null);
                Taxon taxon = new Taxon();
                taxon.setGenus(family.name());
                taxon.setRank(Rank.FAMILY);
                taxon.setIdentifier(family.getIdentifier());
                List<Taxon> children = getService().getGenera(family);
                Set<Taxon> childTaxa = new HashSet<Taxon>();
                childTaxa.addAll(children);
                taxon.setChildTaxa(childTaxa);
                ChangeEvent<Taxon> changeEvent = new ChangeEventImpl<Taxon>(
                        taxon, ChangeType.MODIFIED, new DateTime());
                modelAndView.addObject(AbstractOaiPmhController.OBJECT_KEY,
                        changeEvent);
                switch (metadataPrefix) {
                case RDF:
                    modelAndView
                        .setViewName(AbstractOaiPmhController.RDF_VIEW_NAME);
                    break;
                case OAI_DC:
                default:
                    modelAndView
                        .setViewName(
                            AbstractOaiPmhController.DUBLIN_CORE_VIEW_NAME);
                }
                return modelAndView;
            } catch (NumberFormatException nfe) {
                throw new IdDoesNotExistException(identifier, nfe);
            } catch (IllegalArgumentException iae) {
                throw new IdDoesNotExistException(identifier, iae);
            }
        } else {
            return super.getRecord(identifier, metadataPrefix);
        }
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
