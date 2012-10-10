package org.emonocot.job.oaipmh;

import org.gbif.ecat.voc.NomenclaturalStatus;
import org.gbif.ecat.voc.TaxonomicStatus;
import org.springframework.core.convert.converter.Converter;

/**
 *
 * @author ben
 *
 */
public class TaxonomicStatusConverter implements
        Converter<String, TaxonomicStatus> {

    /**
     * @param identifier
     *            the taxon rank identifier to convert
     * @return a TaxonomicStatus object or null if the taxon status term is null
     */
    public final TaxonomicStatus convert(final String identifier) {
        if (identifier == null || identifier.trim().isEmpty()) {
            return null;
        } else {
            if (identifier
                    .equals("http://e-monocot.org/TaxonomicStatus#accepted")) {
                return TaxonomicStatus.Accepted;
            }  else if (identifier
                    .equals("http://e-monocot.org/TaxonomicStatus#synonym")) {
                return TaxonomicStatus.Synonym;
            } else if (identifier
                    .equals("http://e-monocot.org/TaxonomicStatus#unplaced")) {
                return TaxonomicStatus.Doubtful;
            }  else {
                return TaxonomicStatus.valueOf(identifier);
            }
        }
    }

}
