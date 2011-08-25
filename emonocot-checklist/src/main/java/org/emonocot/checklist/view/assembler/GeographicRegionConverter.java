package org.emonocot.checklist.view.assembler;

import java.net.URI;
import java.net.URISyntaxException;

import org.dozer.CustomConverter;
import org.dozer.MappingException;
import org.emonocot.checklist.model.Distribution;
import org.tdwg.voc.GeographicRegion;

/**
 *
 * @author ben
 *
 */
public class GeographicRegionConverter implements CustomConverter {

   /**
    *
    * @param destination the existing destination field value
    * @param source the source field value
    * @param destClass the destination class
    * @param sourceClass the source class
    * @return a DateTime or null if the date has not been set
    */
    public final Object convert(final Object destination, final Object source,
            final Class destClass, final Class sourceClass) {
        if (source == null) {
            return null;
        }
        if (source instanceof Distribution) {
            Distribution distribution = (Distribution) source;
            GeographicRegion geographicRegion = new GeographicRegion();

            if (distribution.getCountry() != null) {
                geographicRegion.setCode(distribution.getCountry().getCode());
                geographicRegion.setName(distribution.getCountry().getName());

            } else if (distribution.getRegion() != null) {
                geographicRegion.setCode(distribution.getRegion().getCode()
                        .toString());
                geographicRegion.setName(distribution.getRegion().getName());
            }
            if (geographicRegion.getCode() != null) {
                try {
                    geographicRegion.setIdentifier(new URI(
                       "http://rs.tdwg.org/ontology/voc/GeographicRegion.rdf#"
                                    + geographicRegion.getCode()));
                } catch (URISyntaxException use) {
                    throw new MappingException(use);
                }
            }
            return geographicRegion;
        } else {
            throw new MappingException(
                    "Converter GeographicRegionConverter used incorrectly. Arguments passed in were:"
                            + destination + " and " + source);
        }
    }

}
