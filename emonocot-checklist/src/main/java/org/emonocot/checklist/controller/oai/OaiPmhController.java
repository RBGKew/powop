package org.emonocot.checklist.controller.oai;

import java.util.List;

import net.sf.ehcache.Ehcache;

import org.emonocot.checklist.persistence.TaxonDao;
import org.openarchives.pmh.MetadataFormat;
import org.openarchives.pmh.MetadataPrefix;
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
public class OaiPmhController extends AbstractOaiPmhController {

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
}
