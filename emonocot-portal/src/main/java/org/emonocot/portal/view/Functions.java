package org.emonocot.portal.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.emonocot.api.Sorting;
import org.emonocot.api.Sorting.SortDirection;
import org.emonocot.model.description.Distribution;
import org.emonocot.model.description.Feature;
import org.emonocot.model.description.Status;
import org.emonocot.model.description.TextContent;
import org.emonocot.model.geography.Continent;
import org.emonocot.model.geography.Country;
import org.emonocot.model.geography.GeographicalRegion;
import org.emonocot.model.geography.GeographicalRegionComparator;
import org.emonocot.model.geography.Region;
import org.emonocot.model.taxon.Taxon;

/**
 *
 * @author ben
 *
 */
public final class Functions {
    /**
     *
     */
    private Functions() {
    }

    /**
     *
     * @return the list of features
     */
    public static Feature[] features() {
        return Feature.values();
    }

    /**
     *
     * @param taxon
     *            Set the taxon
     * @param feature
     *            Set the feature
     * @return a Content object, or null
     */
    public static TextContent content(
            final Taxon taxon, final Feature feature) {
        return (TextContent) taxon.getContent().get(feature);
    }

    /**
     *
     * @param taxon
     *            Set the taxon
     * @return the list of regions we have distribution records for
     */
    public static List<GeographicalRegion> regions(final Taxon taxon) {
        List<GeographicalRegion> regions = new ArrayList<GeographicalRegion>();
        regions.addAll(taxon.getDistribution().keySet());
        GeographicalRegionComparator comparator = new GeographicalRegionComparator();
        Collections.sort(regions, comparator);
        return regions;
    }

    /**
     * Returns a string which can be passed to the
     * EDIT Map REST Service to produce a distribution map.
     *
     * http://dev.e-taxonomy.eu/trac/wiki/MapRestServiceApi
     *
     * TODO only assumes the status "present" at the moment.
     * TODO assumes that level 3 is the finest level of detail
     *
     * @param taxon Set the taxon
     * @return a string which can be passed to the map rest service
     */
    public static String map(final Taxon taxon) {
        StringBuffer stringBuffer = new StringBuffer();

        List<Continent> continents = new ArrayList<Continent>();
        List<Region> regions = new ArrayList<Region>();
        List<Country> countries = new ArrayList<Country>();

        for (Distribution distribution : taxon.getDistribution().values()) {
            if (distribution.getRegion().getClass().equals(Continent.class)) {
                continents.add((Continent) distribution.getRegion());
            } else if (distribution.getRegion().getClass()
                    .equals(Region.class)) {
                regions.add((Region) distribution.getRegion());
            } else {
                countries.add((Country) distribution.getRegion());
            }
        }
        boolean hasLevel1 = !continents.isEmpty();
        boolean hasLevel2 = !regions.isEmpty();
        boolean hasLevel3 = !countries.isEmpty();

        if (hasLevel1) {
            stringBuffer.append("tdwg1:");
            appendAreas(stringBuffer, Status.present, continents);

            if (hasLevel2 || hasLevel3) {
                stringBuffer.append("||");
            }
        }
        if (hasLevel2) {
            stringBuffer.append("tdwg2:");
            appendAreas(stringBuffer, Status.present, regions);
            if (hasLevel3) {
                stringBuffer.append("||");
            }
        }
        if (hasLevel3) {
            stringBuffer.append("tdwg3:");
            appendAreas(stringBuffer, Status.present, countries);
        }

        return stringBuffer.toString();
    }

    /**
     *
     * @param stringBuffer a stringbuffer containing a partially constructed
     *                     url fragment
     * @param status the status of the taxon in the following regions
     * @param areas a list of regions
     */
    private static void appendAreas(final StringBuffer stringBuffer,
            final Status status,
            final List<? extends GeographicalRegion> areas) {
        stringBuffer.append(status.name() + ":");
        stringBuffer.append(areas.get(0).getCode());

        for (int i = 1; i < areas.size(); i++) {
            stringBuffer.append("," + areas.get(i).getCode());
        }
    }

    /**
     *
     * @return a list of sorting items
     */
    public static List<Sorting> sortItems() {
        List<Sorting> sortItems = new ArrayList<Sorting>();
        sortItems.add(new Sorting("label"));
        sortItems.add(new Sorting("created", SortDirection.REVERSE));
        sortItems.add(new Sorting(null));
        return sortItems;
    }
    /*
    public static String encodeURL(String url){
		return URLEncoder.encodePathSegment(url, UTF8_ENCODING);
    }
    
        public static String decodeURL(String url){
    	return URLDecoder.decodeURL(url);
    }
*/
}
