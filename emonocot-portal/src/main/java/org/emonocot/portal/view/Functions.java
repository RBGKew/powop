package org.emonocot.portal.view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.emonocot.api.FacetName;
import org.emonocot.api.Sorting;
import org.emonocot.api.Sorting.SortDirection;
import org.emonocot.api.convert.ClassToStringConverter;
import org.emonocot.api.convert.PermissionToStringConverter;
import org.emonocot.model.description.Distribution;
import org.emonocot.model.description.Feature;
import org.emonocot.model.description.Status;
import org.emonocot.model.description.TextContent;
import org.emonocot.model.geography.Continent;
import org.emonocot.model.geography.Country;
import org.emonocot.model.geography.GeographicalRegion;
import org.emonocot.model.geography.GeographicalRegionComparator;
import org.emonocot.model.geography.Region;
import org.emonocot.model.identifier.Identifier;
import org.emonocot.model.pager.Page;
import org.emonocot.model.reference.Reference;
import org.emonocot.model.taxon.AlphabeticalTaxonComparator;
import org.emonocot.model.taxon.Rank;
import org.emonocot.model.taxon.Taxon;
import org.emonocot.model.taxon.TaxonomicStatus;
import org.emonocot.portal.view.bibliography.Bibliography;
import org.emonocot.portal.view.bibliography.SimpleBibliographyImpl;
import org.hibernate.proxy.HibernateProxy;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;
import org.springframework.batch.core.BatchStatus;
import org.springframework.core.convert.support.DefaultConversionService;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;

/**
 *
 * @author ben
 *
 */
public final class Functions {
    /**
     *
     */
    private static DefaultConversionService conversionService = new DefaultConversionService();

    static {
        conversionService.addConverter(new PermissionToStringConverter());
        conversionService.addConverter(new ClassToStringConverter());
    }

    /**
     *
     */
    private Functions() {
    }

    /**
     * @param status Set the status
     * @return true if the job is startable
     */
    public static Boolean isStartable(final BatchStatus status) {
        if (status == null) {
            return Boolean.TRUE;
        } else {
            switch (status) {
            case STARTED:
            case STARTING:
            case STOPPING:
            case UNKNOWN:
                return Boolean.FALSE;
            case COMPLETED:
            case FAILED:
            case STOPPED:
            default:
                return Boolean.TRUE;
            }
        }
    }

    /**
     * @param rank Set the rank
     * @return true if the rank is infraspecific
     */
    public static Boolean isInfraspecific(final Rank rank) {
        if (rank == null) {
            return Boolean.FALSE;
        } else {
            if (rank.compareTo(Rank.SPECIES) > 0) {
                return Boolean.TRUE;
            } else {
                return Boolean.FALSE;
            }
        }
    }

    /**
     * @param taxon Set the taxon
     * @return the protolog link of the taxon if it exists
     */
    public static Identifier getProtologLink(final Taxon taxon) {
        for (Identifier identifier : taxon.getIdentifiers()) {
            if (identifier.getSubject().equals("Protolog")) {
                return identifier;
            }
        }
        return null;
    }

    /**
     * @param taxon Set the taxon
     * @return true if the taxon is a synonym
     */
    public static Boolean isSynonym(final Taxon taxon) {
        if (taxon.getStatus() == null) {
            return false;
        } else  {
            return taxon.getStatus().equals(TaxonomicStatus.synonym);
        }
    }

    /**
     *
     * @return the list of features
     */
    public static Feature[] features() {
        return Feature.values();
    }

    /**
     * @param taxon Set the taxon
     * @return the content in the taxon as a set
     */
    public static Collection<TextContent> content(final Taxon taxon) {
        return taxon.getContent().values();
    }

    /**
     * @param region
     *            Set the region
     * @return the country code or null if the distribution is at regional level
     *         or above
     */
    public static String country(final GeographicalRegion region) {
        if (region == null || region.getClass().equals(Region.class)
                || region.getClass().equals(Continent.class)) {
            return null;
        } else {
            return region.getCode().toString();
        }
    }

    /**
     * @param region Set the region
     * @return the region code or null if the distribution is at continent level
     */
    public static String region(final GeographicalRegion region) {
        if (region == null || region.getClass().equals(Continent.class)) {
            return null;
        } else if (region.getClass().equals(Region.class)) {
            return region.getCode().toString();
        } else {
            return ((Country) region).getRegion().getCode().toString();
        }
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
    *
    * @param taxa Set the taxa to sort
    * @return the list of taxa sorted alphabetically
    */
   public static List<Taxon> sort(final Collection<Taxon> taxa) {
       Comparator<Taxon> comparator = new AlphabeticalTaxonComparator();
       List<Taxon> list = new ArrayList<Taxon>(taxa);
       Collections.sort(list, comparator);
       return list;
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

    /**
     *
     * @param object the object, which may be a proxy or may not
     * @return the object
     */
    public static Object deproxy(final Object object) {
        if (object instanceof HibernateProxy) {
            return ((HibernateProxy) object).getHibernateLazyInitializer()
                    .getImplementation();
        } else {
            return object;
        }
    }

    /**
     *
     * @param start Set the start date
     * @param end Set the end date
     * @return a formatted period
     */
    public static String formatPeriod(final Date start, final Date end) {
        DateTime startDate = new DateTime(start);
        DateTime endDate = new DateTime(end);

        Period period = new Interval(startDate, endDate).toPeriod();
        PeriodFormatter formatter = new PeriodFormatterBuilder()
                .minimumPrintedDigits(2).appendHours().appendSeparator(":")
                .appendMinutes().appendSeparator(":").appendSeconds()
                .toFormatter();
        return formatter.print(period);
    }

    /**
    *
    * @param date Set the date
    * @return a formatted date
    */
   public static String formatDate(final Date date) {
       DateTime dateTime = new DateTime(date);
       return DateTimeFormat.forStyle("SS").print(dateTime);
   }

    /**
     *
     * @param list
     *            Set the list
     * @param index
     *            Set the index
     * @return a sublist starting at index
     */
    public static List<Taxon> sublist(final List<Taxon> list,
            final Integer index) {
        return list.subList(index, list.size());
    }

    /**
     *
     * @param string The string to split
     * @param pattern The pattern to split upon
     * @return an array of substrings
     */
    public static List<String> split(final String string, final String pattern) {
        return Arrays.asList(string.split(pattern));
    }

    /**
     *
     * @param object the object to convert
     * @return a string
     */
    public static String convert(final Object object) {
        return conversionService.convert(object, String.class);
    }

    /**
     *
     * @param taxon Set the taxon
     * @return the bibliography
     */
    public static Bibliography bibliography(final Taxon taxon) {
        Bibliography bibliography = new SimpleBibliographyImpl();
        bibliography.setReferences(taxon);
        return bibliography;
    }

   /**
    *
    * @param bibliography Set the bibliography
    * @param reference Set the reference
    * @return the citation key
    */
    public static String citekey(final Bibliography bibliography,
            final Reference reference) {
       return bibliography.getKey(reference);
   }

    /**
    *
    * @param pager Set the pager
    * @param facet Set the facet name
    * @return true, if the facet is selected
    */
    public static Boolean isFacetSelected(final Page pager,
            final String facet) {
       return pager.isFacetSelected(facet);
   }

    /**
    *
    * @param facet Set the facet name
    * @return true, if the facet is multi-valued
    */
    public static Boolean isMultiValued(final String facet) {
       return FacetName.valueOf(facet).isMultivalued();
   }

    /**
     *
     * @param taxon Set the taxon
     * @return the bounding box
     */
   public static String boundingBox(final Taxon taxon) {
        List<Geometry> list = new ArrayList<Geometry>();
        for (Distribution d : taxon.getDistribution().values()) {
            list.add(d.getRegion().getEnvelope());
        }
        GeometryCollection geometryCollection = new GeometryCollection(
                list.toArray(new Geometry[list.size()]), new GeometryFactory());

        Coordinate[] envelope = geometryCollection.getEnvelope()
                .getCoordinates();
        StringBuffer boundingBox = new StringBuffer();
        boundingBox.append(Math.round(envelope[0].x));
        boundingBox.append(",");
        boundingBox.append(Math.round(envelope[0].y));
        boundingBox.append(",");
        boundingBox.append(Math.round(envelope[2].x));
        boundingBox.append(",");
        boundingBox.append(Math.round(envelope[2].y));
        return boundingBox.toString();
   }

   /**
    *
    * @param taxon Set the taxon
    * @return true if the taxon has level 1 features, false otherwise
    */
    public static Boolean hasLevel1Features(final Taxon taxon) {
        for (Distribution d : taxon.getDistribution().values()) {
            if (d.getRegion() instanceof Continent) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    /**
    *
    * @param taxon Set the taxon
    * @return the level 1 feature identifiers (FIDs)
    */
    public static String getLevel1Features(final Taxon taxon) {
        boolean first = true;
        StringBuffer features = new StringBuffer();
        for (Distribution d : taxon.getDistribution().values()) {
            if (d.getRegion() instanceof Continent) {
                if (!first) {
                    features.append(",");
                }
                features.append(d.getRegion().getFeatureId());
                first = false;
            }
        }
        return features.toString();
    }

    /**
    *
    * @param taxon Set the taxon
    * @return true if the taxon has level 2 features, false otherwise
    */
    public static Boolean hasLevel2Features(final Taxon taxon) {
        for (Distribution d : taxon.getDistribution().values()) {
            if (d.getRegion() instanceof Region) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    /**
    *
    * @param taxon Set the taxon
    * @return the level 2 feature identifiers (FIDs)
    */
    public static String getLevel2Features(final Taxon taxon) {
        boolean first = true;
        StringBuffer features = new StringBuffer();
        for (Distribution d : taxon.getDistribution().values()) {
            if (d.getRegion() instanceof Region) {
                if (!first) {
                    features.append(",");
                }
                features.append(d.getRegion().getFeatureId());
                first = false;
            }
        }
        return features.toString();
    }

    /**
    *
    * @param taxon Set the taxon
    * @return true if the taxon has level 3 features, false otherwise
    */
    public static Boolean hasLevel3Features(final Taxon taxon) {
        for (Distribution d : taxon.getDistribution().values()) {
            if (d.getRegion() instanceof Country) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    /**
    *
    * @param taxon Set the taxon
    * @return the level 3 feature identifiers (FIDs)
    */
    public static String getLevel3Features(final Taxon taxon) {
        boolean first = true;
        StringBuffer features = new StringBuffer();
        for (Distribution d : taxon.getDistribution().values()) {
            if (d.getRegion() instanceof Country) {
                if (!first) {
                    features.append(",");
                }
                features.append(d.getRegion().getFeatureId());
                first = false;
            }
        }
        return features.toString();
    }
}
