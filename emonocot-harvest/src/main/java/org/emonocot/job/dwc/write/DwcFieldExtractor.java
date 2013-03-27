package org.emonocot.job.dwc.write;

import java.util.Map;

import org.emonocot.api.job.DarwinCorePropertyMap;
import org.emonocot.api.job.TermFactory;
import org.emonocot.model.BaseData;
import org.gbif.dwc.terms.ConceptTerm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.file.transform.FieldExtractor;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.NullValueInNestedPathException;
import org.springframework.beans.PropertyAccessException;
import org.springframework.core.convert.ConversionService;
import org.springframework.util.Assert;

public class DwcFieldExtractor implements FieldExtractor<BaseData> {
	
	private static Logger logger = LoggerFactory.getLogger(DwcFieldExtractor.class);
	
	private String[] names;
	
	private String extension;
	
	private Character quoteCharacter;
	
	private TermFactory termFactory = new TermFactory();
	
    private ConversionService conversionService;
    
    public void setQuoteCharacter(Character quoteCharacter) {
    	this.quoteCharacter = quoteCharacter;
    }
    
    public void setConversionService(ConversionService conversionService) {
    	this.conversionService = conversionService;
    }
	
	public void setNames(String[] names) {
		Assert.notNull(names, "Names must be non-null");
		this.names = names;
	}
	
	public void setExtension(String extension) {
		Assert.notNull(extension, "Extension must be non-null");
		this.extension = extension;
	}
	
	
	@Override
	public Object[] extract(BaseData item) {
		Object[] values = new Object[names.length];
		ConceptTerm extensionTerm = termFactory.findTerm(extension);
		Map<ConceptTerm,String> propertyMap = DarwinCorePropertyMap.getPropertyMap(extensionTerm);		
		BeanWrapper beanWrapper = new BeanWrapperImpl(item);
		for(int i = 0; i < names.length; i++) {
			String property = names[i];			
			ConceptTerm propertyTerm = termFactory.findTerm(property);			
		    String propertyName = propertyMap.get(propertyTerm);
		     try {
		    	 String value = conversionService.convert(beanWrapper.getPropertyValue(propertyName), String.class);
		    	 if(quoteCharacter == null) {
		    	     values[i] = value;
		    	 } else if(value != null) {
		    		 values[i] = new StringBuilder().append(quoteCharacter).append(value).append(quoteCharacter).toString();
		    	 } else {
		    		 values[i] = new StringBuilder().append(quoteCharacter).append(quoteCharacter).toString();
		    	 }
		     } catch(PropertyAccessException pae) {
		    	 if(quoteCharacter != null) {
		    		 values[i] = new StringBuilder().append(quoteCharacter).append(quoteCharacter).toString();
		    	 }
		     } catch(NullValueInNestedPathException nvinpe) {
		    	 if(quoteCharacter != null) {
		    		 values[i] = new StringBuilder().append(quoteCharacter).append(quoteCharacter).toString();
		    	 }
		     }
		     
		}
		return values;
	}

}
