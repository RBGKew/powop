package org.emonocot.job.dwc.write;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.emonocot.job.dwc.DarwinCorePropertyMap;
import org.emonocot.model.BaseData;
import org.gbif.dwc.terms.ConceptTerm;
import org.gbif.dwc.terms.TermFactory;
import org.springframework.batch.item.file.transform.FieldExtractor;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.PropertyAccessException;
import org.springframework.core.convert.ConversionService;
import org.springframework.util.Assert;

public class DwcFieldExtractor implements FieldExtractor<BaseData> {
	
	private String[] names;
	
	private String extension;
	
	private TermFactory termFactory = new TermFactory();
	
    private ConversionService conversionService;
    
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
		List<Object> values = new ArrayList<Object>();
		ConceptTerm extensionTerm = termFactory.findTerm(extension);
		Map<ConceptTerm,String> propertyMap = DarwinCorePropertyMap.getPropertyMap(extensionTerm);
		BeanWrapper beanWrapper = new BeanWrapperImpl(item);
		for(String property : names) {
		     ConceptTerm propertyTerm = termFactory.findTerm(property);
		     String propertyName = propertyMap.get(propertyTerm);
		     try {
		    	 values.add(conversionService.convert(beanWrapper.getPropertyValue(propertyName), String.class));
		     } catch(PropertyAccessException pae) {
		    	 
		     }
		}
		return values.toArray();
	}

}
