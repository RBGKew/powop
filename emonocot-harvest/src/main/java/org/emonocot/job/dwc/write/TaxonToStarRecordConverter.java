/**
 * 
 */
package org.emonocot.job.dwc.write;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.emonocot.api.job.DarwinCorePropertyMap;
import org.emonocot.model.Description;
import org.emonocot.model.Distribution;
import org.emonocot.model.Identifier;
import org.emonocot.model.Image;
import org.emonocot.model.MeasurementOrFact;
import org.emonocot.model.Reference;
import org.emonocot.model.Taxon;
import org.emonocot.model.TypeAndSpecimen;
import org.emonocot.model.VernacularName;
import org.gbif.dwc.record.Record;
import org.gbif.dwc.terms.ConceptTerm;
import org.gbif.dwc.terms.DwcTerm;
import org.gbif.dwc.terms.GbifTerm;
import org.gbif.dwc.terms.TermFactory;
import org.gbif.dwc.text.StarRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.PropertyAccessException;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;

/**
 * @author jk00kg
 * 
 */
public class TaxonToStarRecordConverter implements
        Converter<Taxon, StarRecord>, ItemProcessor<Taxon, StarRecord> {

	public void setIdentifierColumns(String[] identifierColumns) {
		this.identifierColumns = identifierColumns;
	}

	public void setMeasurementOrFactColumns(String[] measurementOrFactColumns) {
		this.measurementOrFactColumns = measurementOrFactColumns;
	}

	public void setTypeAndSpecimenColumns(String[] typeAndSpecimenColumns) {
		this.typeAndSpecimenColumns = typeAndSpecimenColumns;
	}

	public void setVernacularNameColumns(String[] vernacularNameColumns) {
		this.vernacularNameColumns = vernacularNameColumns;
	}

	public void setImageColumns(String[] imageColumns) {
		this.imageColumns = imageColumns;
	}

	/**
     * 
     */
    private static final Logger logger = LoggerFactory.getLogger(TaxonToStarRecordConverter.class);

    /**
     * A set of extensions to expect in preference to detecting them as data is read
     */
    private String[] extensions = new String[0];
    
    private String[] taxonColumns = new String[0]; 
    
    private String[] distributionColumns = new String[0]; 
    
    private String[] descriptionColumns = new String[0]; 
    
    private String[] referenceColumns = new String[0];
    
    private String[] imageColumns = new String[0];
    
    private String[] identifierColumns = new String[0];
    
    private String[] measurementOrFactColumns = new String[0];
    
    private String[] typeAndSpecimenColumns = new String[0];
    
    private String[] vernacularNameColumns = new String[0];
    

    /**
     * 
     */
    private TermFactory termFactory = new TermFactory();
    
    private ConversionService conversionService;    
    
    /**
     * @param extensions the extensions to write into the StarRecords
     */
    public final void setExtensions(String[] extensions) {        
        this.extensions = extensions;
    }

    /**
     * @param termFactory the termFactory to set
     */
    public void setTermFactory(TermFactory termFactory) {
        if(termFactory != null){
            this.termFactory = termFactory;
        }
    }
    
	public void setTaxonColumns(String[] taxonColumns) {
		this.taxonColumns = taxonColumns;
	}

	public void setDistributionColumns(String[] distributionColumns) {
		this.distributionColumns = distributionColumns;
	}

	public void setDescriptionColumns(String[] descriptionColumns) {
		this.descriptionColumns = descriptionColumns;
	}

	public void setReferenceColumns(String[] referenceColumns) {
		this.referenceColumns = referenceColumns;
	}
    
    public void setConversionService(ConversionService conversionService) {
		this.conversionService = conversionService;
	}
    
    private SimpleRecord mapItem(ConceptTerm recordType, Object object, String idPropertyName, String[] properties, Map<ConceptTerm,String> propertyMap) {
    	SimpleRecord record = new SimpleRecord(recordType);
    	BeanWrapper beanWrapper = new BeanWrapperImpl(object);
    	if(idPropertyName != null) {
    	    record.setId(conversionService.convert(beanWrapper.getPropertyValue(idPropertyName), String.class));
    	}
    	
    	for(String property : properties) {
    		// First convert the property into a string
    		ConceptTerm conceptTerm = termFactory.findTerm(property);
    		// Then provided the property is valid for this object  		
    		if(propertyMap.containsKey(conceptTerm)) {
    		    try { 
    		    	// Get the actual object value
    		        Object value = beanWrapper.getPropertyValue(propertyMap.get(conceptTerm));
    		        // And convert it to a string if neccessary
    		        record.setProperty(conceptTerm, conversionService.convert(value,String.class));
    		    } catch (PropertyAccessException pae) {
    		    	// Or skip it if neccessary
    		    }
    		}
    	}
    	
    	return record;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.springframework.batch.item.ItemProcessor#process(java.lang.Object)
     */
    @Override
    public StarRecord process(Taxon item) throws Exception {
        
        if (item == null) {
        	//we can't process this
        	return null;
        }
        Collection<String> extensionsCollection = new ArrayList<String>();
        for(String extension : extensions) {
            extensionsCollection.add(extension);
        }
        StarRecord star = new StarRecord(extensionsCollection);
        //Core record with at least an identifier
        
        star.newCoreRecord(mapItem(DwcTerm.Taxon, item, "identifier",taxonColumns, DarwinCorePropertyMap.taxonTerms));
        
        //Check what data we have
        if(extensionsCollection.contains("Distribution") && item.getDistribution().size() > 0) {        	
            star.extensions().put(GbifTerm.Distribution.simpleName(), new ArrayList<Record>());
            for( Distribution distribution : item.getDistribution()) {
                star.addRecord(GbifTerm.Distribution.simpleName(), mapItem(GbifTerm.Distribution, distribution, null,distributionColumns, DarwinCorePropertyMap.distributionTerms));
            }
        }
        if(extensionsCollection.contains("Description") && item.getDescriptions().size() > 0) {
            star.extensions().put(GbifTerm.Description.simpleName(), new ArrayList<Record>());
            for(Description description : item.getDescriptions()) {
                star.addRecord(GbifTerm.Description.simpleName(), mapItem(GbifTerm.Description, description, null,descriptionColumns, DarwinCorePropertyMap.descriptionTerms));
            }
        }
        if(extensionsCollection.contains("Reference") && item.getReferences().size() > 0) {
            star.extensions().put(GbifTerm.Reference.simpleName(), new ArrayList<Record>());
            for(Reference reference : item.getReferences()) {
                star.addRecord(GbifTerm.Reference.simpleName(), mapItem(GbifTerm.Reference, reference, null,referenceColumns, DarwinCorePropertyMap.referenceTerms));
            }
        }
        if(extensionsCollection.contains("Image") && item.getImages().size() > 0) {
            star.extensions().put(GbifTerm.Image.simpleName(), new ArrayList<Record>());
            for(Image image : item.getImages()) {
                star.addRecord(GbifTerm.Image.simpleName(), mapItem(GbifTerm.Image, image, null,imageColumns, DarwinCorePropertyMap.imageTerms));
            }
        }
        if(extensionsCollection.contains("Identifier") && item.getIdentifiers().size() > 0) {
            star.extensions().put(GbifTerm.Identifier.simpleName(), new ArrayList<Record>());
            for(Identifier identifier : item.getIdentifiers()) {
                star.addRecord(GbifTerm.Identifier.simpleName(), mapItem(GbifTerm.Identifier, identifier, null,identifierColumns, DarwinCorePropertyMap.identifierTerms));
            }
        }
        if(extensionsCollection.contains("MeasurementOrFact") && item.getMeasurementsOrFacts().size() > 0) {
            star.extensions().put(DwcTerm.MeasurementOrFact.simpleName(), new ArrayList<Record>());
            for(MeasurementOrFact measurementOrFact : item.getMeasurementsOrFacts()) {
                star.addRecord(DwcTerm.MeasurementOrFact.simpleName(), mapItem(DwcTerm.MeasurementOrFact, measurementOrFact, null,measurementOrFactColumns, DarwinCorePropertyMap.measurementOrFactTerms));
            }
        }
        if(extensionsCollection.contains("TypeAndSpecimen") && item.getTypesAndSpecimens().size() > 0) {
            star.extensions().put(GbifTerm.TypesAndSpecimen.simpleName(), new ArrayList<Record>());
            for(TypeAndSpecimen typeAndSpecimen : item.getTypesAndSpecimens()) {
                star.addRecord(GbifTerm.TypesAndSpecimen.simpleName(), mapItem(GbifTerm.TypesAndSpecimen, typeAndSpecimen, null,typeAndSpecimenColumns, DarwinCorePropertyMap.typeAndSpecimenTerms));
            }
        }
        if(extensionsCollection.contains("VernacularName") && item.getVernacularNames().size() > 0) {
            star.extensions().put(GbifTerm.VernacularName.simpleName(), new ArrayList<Record>());
            for(VernacularName vernacularName : item.getVernacularNames()) {
                star.addRecord(GbifTerm.VernacularName.simpleName(), mapItem(GbifTerm.VernacularName, vernacularName, null,vernacularNameColumns, DarwinCorePropertyMap.vernacularNameTerms));
            }
        }
        
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
