package org.emonocot.portal.view;

import java.util.ArrayList;
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
import org.emonocot.model.pager.Page;
import org.emonocot.model.reference.Reference;
import org.emonocot.model.taxon.AlphabeticalTaxonComparator;
import org.emonocot.model.taxon.Taxon;
import org.emonocot.portal.view.bibliography.Bibliography;
import org.emonocot.portal.view.bibliography.SimpleBibliographyImpl;
import org.hibernate.proxy.HibernateProxy;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;
import org.springframework.core.convert.support.DefaultConversionService;

import com.rc.retroweaver.runtime.Arrays;

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
}
