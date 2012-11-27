/**
 * 
 */
package org.emonocot.job.dwc;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.emonocot.model.Distribution;
import org.emonocot.model.Reference;
import org.emonocot.model.Taxon;
import org.gbif.dwc.record.Record;
import org.gbif.dwc.terms.DcTerm;
import org.gbif.dwc.terms.DwcTerm;
import org.gbif.dwc.terms.GbifTerm;
import org.gbif.dwc.terms.TermFactory;
import org.gbif.dwc.text.StarRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.core.convert.converter.Converter;

/**
 * @author jk00kg
 * 
 */
public class TaxonToStarRecordConverter implements
        Converter<Taxon, StarRecord>, ItemProcessor<Taxon, StarRecord> {

    /**
     * 
     */
    private static final Logger logger = LoggerFactory.getLogger(TaxonToStarRecordConverter.class);

    /**
     * A set of extensions to expect in preference to detecting them as data is read
     */
    private Set<String> extensions = new HashSet<String>();

    /**
     * 
     */
    private TermFactory termFactory = new TermFactory();

    /**
     * @param extensions the extensions to write into the StarRecords
     */
    public final void setExtensions(Set<String> extensions) {
        if(extensions != null){
            this.extensions.addAll(extensions);
        }
    }

    /**
     * @param termFactory the termFactory to set
     */
    public void setTermFactory(TermFactory termFactory) {
        if(termFactory != null){
            this.termFactory = termFactory;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.springframework.batch.item.ItemProcessor#process(java.lang.Object)
     */
    @Override
    public StarRecord process(Taxon item) throws Exception {
        StarRecord star = new StarRecord(extensions);
        if (item == null) {
        	//we can't process this
        	return null;
        }
        
        //Core record with at least an identifier
        SimpleRecord core = new SimpleRecord(DwcTerm.Taxon);
        core.setId(item.getIdentifier());
        core.setProperty(DwcTerm.scientificName, StringUtils.isBlank(item.getScientificName()) ?
        		null : item.getScientificName());
        core.setProperty(DcTerm.modified, item.getModified() == null  ||
        		StringUtils.isBlank(item.getModified().toString()) ?
        				null : item.getModified().toString());
        if(item.getAcceptedNameUsage() != null) {
            core.setProperty(DwcTerm.acceptedNameUsageID, item.getAcceptedNameUsage().getIdentifier());
            core.setProperty(DwcTerm.acceptedNameUsage, item.getAcceptedNameUsage().getScientificName());
        }
        if(item.getParentNameUsage() != null) {
            core.setProperty(DwcTerm.parentNameUsageID, item.getParentNameUsage().getIdentifier());
            core.setProperty(DwcTerm.parentNameUsage, item.getParentNameUsage().getScientificName());
        }
        core.setProperty(DwcTerm.namePublishedIn, StringUtils.isBlank(item.getNamePublishedInString()) ?
        		null : item.getNamePublishedInString());
        if(item.getNamePublishedInYear() != null) {
        		core.setProperty(DwcTerm.namePublishedInYear, item.getNamePublishedInYear().toString());
        }
        List<String> higherClassificationEpithets = new ArrayList<String>();
        for(Taxon t : item.getHigherClassification()){
            higherClassificationEpithets.add(t.getScientificName());
        }
        core.setProperty(DwcTerm.higherClassification, StringUtils.join(higherClassificationEpithets, ";"));
        if(item.getFamily() != null) {
        	core.setProperty(DwcTerm.family, item.getFamily());
        }
        if(item.getGenus() != null) {
        	core.setProperty(DwcTerm.genus, item.getGenus());
        }
        if(item.getSpecificEpithet() != null) {
        	core.setProperty(DwcTerm.specificEpithet, item.getSpecificEpithet());
        }
        if(item.getInfraspecificEpithet() != null) {
        	core.setProperty(DwcTerm.infraspecificEpithet, item.getInfraspecificEpithet());
        }
        if(item.getTaxonRank() != null) {
        	core.setProperty(DwcTerm.taxonRank, item.getTaxonRank().toString());
        }
        if(item.getScientificNameAuthorship() != null) {
        	core.setProperty(DwcTerm.scientificNameAuthorship, item.getScientificNameAuthorship());
        }
        //TODO other core fields e.g.remarks
        star.newCoreRecord(core);
        
        //Check what data we have
        if(extensions.size() < 1) { //If extensions haven't been specified
        	if(item.getDistribution().size() > 0) {
                star.extensions().put(GbifTerm.Distribution.simpleName(), new ArrayList<Record>());
            }
        	if(item.getReferences().size() > 0) {
                star.extensions().put(GbifTerm.Reference.simpleName(), new ArrayList<Record>());
            }
            
            
        }
        //Distribution
        for( Distribution distribution : item.getDistribution()) {
            SimpleRecord starDistribution = new SimpleRecord(GbifTerm.Distribution);
//            TODO clean up use of TDWG codes and potentially have converters for other objects
            starDistribution.setProperty(DwcTerm.locationID, "TDWG:" + distribution.getLocation().getCode());
            star.addRecord(starDistribution.getRowType().simpleName(), starDistribution);
        }
        //References
        for(Reference ref : item.getReferences()) {
            SimpleRecord starReference = new SimpleRecord(GbifTerm.Reference); 
            if(ref.getIdentifier() != null) {
                starReference.setProperty(DcTerm.identifier, ref.getIdentifier());
            }
            starReference.setProperty(DcTerm.bibliographicCitation, ref.getBibliographicCitation());
            starReference.setProperty(DcTerm.title, ref.getTitle());
            starReference.setProperty(DcTerm.creator, ref.getCreator());
            starReference.setProperty(DcTerm.date, ref.getDate());
            starReference.setProperty(DcTerm.source, ref.getSource());
            starReference.setProperty(DcTerm.subject, ref.getSubject());
            starReference.setProperty(DcTerm.type, ref.getType().toString());
        }
        
        SimpleRecord description = new SimpleRecord(GbifTerm.Description);
        description.setId("urn:example.com:Record:desc:1");
        description.setProperty(DcTerm.type, "habitat");
        description.setProperty(DcTerm.description, "Dark basements, also known in server rooms and " +
                "other localities with high socket counts");
        //TODO MeasurementOrFact?!?
        return star;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.springframework.core.convert.converter.Converter#convert(java.lang.Object)
     */
    @Override
    public StarRecord convert(Taxon source) {
        try {
            return process(source);
        } catch (Exception e) {
            logger.error("Unable to convert to StarRecord", e);
            return null;
        }

    }

}
