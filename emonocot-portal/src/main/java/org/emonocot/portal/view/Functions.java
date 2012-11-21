package org.emonocot.portal.view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.el.ELException;

import org.emonocot.api.convert.ClassToStringConverter;
import org.emonocot.api.convert.PermissionToStringConverter;
import org.emonocot.model.Distribution;
import org.emonocot.model.Identifier;
import org.emonocot.model.Image;
import org.emonocot.model.MeasurementOrFact;
import org.emonocot.model.Reference;
import org.emonocot.model.Source;
import org.emonocot.model.Taxon;
import org.emonocot.model.Description;
import org.emonocot.model.TypeAndSpecimen;
import org.emonocot.model.VernacularName;
import org.emonocot.model.constants.DescriptionType;
import org.emonocot.model.constants.Status;
import org.emonocot.model.geography.Continent;
import org.emonocot.model.geography.Country;
import org.emonocot.model.geography.GeographicalRegion;
import org.emonocot.model.geography.GeographicalRegionComparator;
import org.emonocot.model.geography.Region;
import org.emonocot.model.util.AlphabeticalTaxonComparator;
import org.emonocot.pager.FacetName;
import org.emonocot.pager.Page;
import org.emonocot.portal.view.bibliography.Bibliography;
import org.emonocot.portal.view.bibliography.SimpleBibliographyImpl;
import org.gbif.ecat.voc.Rank;
import org.gbif.ecat.voc.TaxonomicStatus;
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

    /**
     *
     */
    private static double MAX_DEGREES_LATITUDE = 180.0;

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
    * @param string Set the string to strip xml from
    * @return an stripped string
    */
   public static String stripXml(final String string) {
       return string.replaceAll("\\<.*?>","");
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
    
    public static String evaluate(String expressionString, PageContext pageContext) throws ELException {
    	return (String)pageContext.getExpressionEvaluator().evaluate(expressionString, String.class, pageContext.getVariableResolver(), null);
    }

    /**
     *
     * @param rank Set the rank
     * @return the abbreviated rank
     */
    public static String abbreviateRank(final Rank rank) {
    	if(rank == null) {
    		return null;
    	} else {
    		switch(rank) {
    		case VARIETY:
    			return "var";
    		case InfraspecificName:
    			return "infrasp";
    		case SUBSPECIES:
    			return "ssp";
    		case SPECIES:
    			return "sp";
    		case GENUS:
    			return "gen";
    		case Subtribe:
    			return "subtrib";
    		case Tribe:
    			return "trib";
    		case Subfamily:
    			return "subfam";
    		case FAMILY:
    			return "fam";
    		default:
    			return Rank.toAbbreviation(rank);
    	
    		}
    	}
    }
      
    /**
    *
    * @param rank Set the rank
    * @return the formatted rank
    */
   public static String formatRank(final Rank rank) {
   	if(rank == null) {
   		return null;
   	} else {
   		String r = rank.name();
   		return r.substring(0,1).toUpperCase() + r.substring(1).toLowerCase();
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
            if (identifier.getSubject() != null && identifier.getSubject().equals("Protolog")) {
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
        if (taxon.getTaxonomicStatus() == null) {
            return false;
        } else  {
            return taxon.getTaxonomicStatus().equals(TaxonomicStatus.Synonym);
        }
    }
    /**
     * @param taxon Set the taxon
     * @return true if the taxon is accepted
     */
    public static Boolean isAccepted(final Taxon taxon) {
        if (taxon.getTaxonomicStatus() == null) {
            return false;
        } else  {
            return taxon.getTaxonomicStatus().equals(TaxonomicStatus.Accepted);
        }
    }

    /**
     *
     * @return the list of features
     */
    public static DescriptionType[] features() {
        return DescriptionType.values();
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
    public static Description content(
            final Taxon taxon, final DescriptionType feature) {
    	Description description = null;
    	for(Description d : taxon.getDescriptions()) {
    		if(d.getType().equals(feature)) {
    			description = d;
    			break;
    		}
    	}
        return description;
    }

    /**
     *
     * @param taxon
     *            Set the taxon
     * @return the list of regions we have distribution records for
     */
    public static List<GeographicalRegion> regions(final Taxon taxon) {
        List<GeographicalRegion> regions = new ArrayList<GeographicalRegion>();
        for(Distribution d : taxon.getDistribution()) {
        	regions.add(d.getLocation());
        }
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

        for (Distribution distribution : taxon.getDistribution()) {
            if (distribution.getLocation().getClass().equals(Continent.class)) {
                continents.add((Continent) distribution.getLocation());
            } else if (distribution.getLocation().getClass()
                    .equals(Region.class)) {
                regions.add((Region) distribution.getLocation());
            } else {
                countries.add((Country) distribution.getLocation());
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
    public static List<String> sortItems() {
        List<String> sortItems = new ArrayList<String>();
        sortItems.add("searchable.label_sort_asc");
        sortItems.add("base.created_dt_desc");
        sortItems.add("_asc");
        return sortItems;
    }
    
    /**
    *
    * @return a list of sorting items
    */
   public static List<String> annotationSortItems() {
       List<String> sortItems = new ArrayList<String>();
       sortItems.add("annotation.type_s_asc");
       sortItems.add("annotation.code_s_asc");
       sortItems.add("annotation.record_type_s_asc");
       sortItems.add("base.created_dt_desc");
       sortItems.add("_asc");
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
    * To cope with the fact that we can't use the reserved word 'class' in
    * JSP Expression language and Tomcat 7 complains.
    *
    * @param object the object to convert
    * @return the fully qualified class name
    */
   public static String convertClazz(final Object object) {
       return conversionService.convert(object.getClass(), String.class);
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
    
   public static String escapeHtmlIdentifier(String identifier) {
	   return identifier.replaceAll("\\.", "").replaceAll("_", "");
   }
    
   public static Set<Source> sources(Taxon taxon) {
	   Set<Source> sources = new HashSet<Source>();
	   sources.add(taxon.getAuthority());
	   for(Description d : taxon.getDescriptions()) {
		   sources.add(d.getAuthority());
	   }
	   
	   for(Distribution d : taxon.getDistribution()) {
		   sources.add(d.getAuthority());
	   }
	   
	   for(Image i : taxon.getImages()) {
		   sources.add(i.getAuthority());
	   }
	   
	   for(Reference r : taxon.getReferences()) {
		   sources.add(r.getAuthority());
	   }
	   
	   for(Identifier i : taxon.getIdentifiers()) {
		   sources.add(i.getAuthority());
	   }
	   
	   for(TypeAndSpecimen s : taxon.getTypesAndSpecimens()) {
		   sources.add(s.getAuthority());
	   }
	   
	   for(MeasurementOrFact f : taxon.getMeasurementsOrFacts()) {
		   sources.add(f.getAuthority());
	   }
	   
	   for(VernacularName n : taxon.getVernacularNames()) {
		   sources.add(n.getAuthority());
	   }
	   
	   return sources;
   }

    /**
     *
     * @param taxon Set the taxon
     * @return the bounding box
     */
   public static String boundingBox(final Taxon taxon) {
        List<Geometry> list = new ArrayList<Geometry>();
        for (Distribution d : taxon.getDistribution()) {
            list.add(d.getLocation().getEnvelope());
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
        for (Distribution d : taxon.getDistribution()) {
            if (d.getLocation() instanceof Continent) {
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
        for (Distribution d : taxon.getDistribution()) {
            if (d.getLocation() instanceof Continent) {
                if (!first) {
                    features.append(",");
                }
                features.append(d.getLocation().getFeatureId());
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
        for (Distribution d : taxon.getDistribution()) {
            if (d.getLocation() instanceof Region) {
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
        for (Distribution d : taxon.getDistribution()) {
            if (d.getLocation() instanceof Region) {
                if (!first) {
                    features.append(",");
                }
                features.append(d.getLocation().getFeatureId());
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
        for (Distribution d : taxon.getDistribution()) {
            if (d.getLocation() instanceof Country) {
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
        for (Distribution d : taxon.getDistribution()) {
            if (d.getLocation() instanceof Country) {
                if (!first) {
                    features.append(",");
                }
                features.append(d.getLocation().getFeatureId());
                first = false;
            }
        }
        return features.toString();
    }
}
