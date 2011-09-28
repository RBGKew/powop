package org.emonocot.portal.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.emonocot.model.description.Status;
import org.emonocot.model.description.TextContent;
import org.emonocot.model.description.Distribution;
import org.emonocot.model.description.Feature;
import org.emonocot.model.geography.GeographicalRegion;
import org.emonocot.model.geography.Continent;
import org.emonocot.model.geography.Region;
import org.emonocot.model.geography.Country;
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
     * @return
     */
    public static Feature[] features() {
        return Feature.values();
    }

    /**
     *
     * @param taxon Set the taxon
     * @param feature Set the feature
     * @return a Content object, or null
     */
    public static TextContent content(Taxon taxon, Feature feature) {
        return (TextContent)taxon.getContent().get(feature);
    }
    
    /**
     *
     * @param taxon Set the taxon
     * @return the list of regions we have distribution records for
     */
    public static Set<GeographicalRegion> regions(Taxon taxon) {
        return taxon.getDistribution().keySet();        
    }
    /**
     * TODO only assumes the status "present" at the moment
     * @param taxon
     * @return
     */
    public static String getMapParam(Taxon taxon) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("&ad=");
        
        
        List<Continent> continents = new ArrayList<Continent>();
        List<Region> regions = new ArrayList<Region>();
        List<Country> countries = new ArrayList<Country>();
        
        
        for(Distribution distribution : taxon.getDistribution().values()) {
                
        }
        boolean hasLevel1 = !continents.isEmpty();
        boolean hasLevel2 = !regions.isEmpty();
        boolean hasLevel3 = !countries.isEmpty();
        
        if(hasLevel1) {
            stringBuffer.append("tdwg1:");
            appendAreas(stringBuffer,Status.present,continents);

            if(hasLevel2 || hasLevel3) {
                stringBuffer.append("||");
            }
        }
        if(hasLevel2) {
            stringBuffer.append("tdwg2:");
            appendAreas(stringBuffer,Status.present,regions);
            if(hasLevel3) {
                stringBuffer.append("||");
            }
        }
        if(hasLevel3) {
            stringBuffer.append("tdwg3:");
            appendAreas(stringBuffer,Status.present,countries);
        }
        
        stringBuffer.append("&as=");
        
        stringBuffer.append(getStyleCode(Status.present));
        stringBuffer.append(":");
        stringBuffer.append(getStyleColor(Status.present));
        stringBuffer.append(",,0.25");
                
        return stringBuffer.toString();
    }
    
    private static void appendAreas(StringBuffer stringBuffer, Status status, List<? extends GeographicalRegion> areas) {
        stringBuffer.append(getStyleCode(status) + ":");
        stringBuffer.append(areas.get(0).getCode());
        
        for(int i = 1; i < areas.size(); i++) {
                stringBuffer.append("," + areas.get(i).getCode());
        }
    }
    
    private static String getStyleCode(Status status) {
        switch(status) {
        case present:
            return "p";
        case common:
            return "c";
        case rare:
            return "r";
        case irregular:
            return "i";
        case doubtful:
            return "d";
        case absent:
            return "a";
        case excluded:
            return "e";
         default:
             return null;            
        }
    }
    
    private static String getStyleColor(Status status) {
        switch(status) {
        case present:
            return "FF0000";
        case common:
            return "0D3D70";
        case rare:
            return "007700";
        case irregular:
            return "400000";
        case doubtful:
            return "3E7126";
        case absent:
            return "A40778";
        case excluded:
            return "5081B5";
         default:
             return null;            
        }
    }
}
